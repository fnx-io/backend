package io.fnx.backend.service.impl;

import io.fnx.backend.auth.AllowedForTrusted;
import io.fnx.backend.domain.Role;
import io.fnx.backend.domain.UniqueProps;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.user.UpdateUserDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.domain.dto.login.InvalidCredentialsLoginResult;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.domain.filter.user.ListUsersFilter;
import io.fnx.backend.service.*;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;
import io.fnx.backend.domain.AuthTokenEntity;
import io.fnx.backend.manager.AuthTokenManager;
import io.fnx.backend.manager.UniqueIndexManager;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.authorization.AllowedForAuthenticated;
import io.fnx.backend.tools.authorization.AllowedForOwner;
import io.fnx.backend.util.MessageAccessor;
import org.joda.time.Duration;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static io.fnx.backend.tools.ofy.OfyUtils.keyToId;
import static java.lang.String.format;

public class UserServiceImpl extends BaseService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private AuthTokenManager<UserEntity> authTokenManager;
    private UniqueIndexManager uniqueIndexManager;
    private MailService mailService;

    @Override
    @AllowedForAdmins
    @AllowedForTrusted
    public UserEntity createUser(UserDto cmd) {
        UserEntity result = createUserImpl(cmd);
	    Map<String, Object> params = new HashMap<>();
	    params.put("email", result.getEmail());
	    params.put("password", cmd.getPassword());
        mailService.sendEmail(result, messageAccessor.getMessage("email.subject.userCreated"), "user-created", params);
        return result;
    }

	@Override
	public UserEntity registerUser(UserDto cmd) {
		UserEntity result = createUserImpl(cmd);
		Map<String, Object> params = new HashMap<>();
		params.put("email", result.getEmail());
		mailService.sendEmail(result, messageAccessor.getMessage("email.subject.userRegistered"), "user-registered", params);
		return result;
	}

	private UserEntity createUserImpl(UserDto cmd) {
		checkNotNull(cmd, "User must not be null");
		final Key<UserEntity> userKey = ofy().factory().allocateId(UserEntity.class);
		final UserEntity user = new UserEntity();
		user.setId(userKey.getId());
		user.setEmail(cmd.getEmail());
		user.setFirstName(cmd.getFirstName());
		user.setLastName(cmd.getLastName());
		user.setPasswordHash(hashPassword(cmd.getPassword()));
		if (cc().isAdmin()) {
			user.setRole(cmd.getRole());
		}
		if (user.getRole() == null) {
			user.setRole(Role.USER);
		}
		return ofy().transact(new Work<UserEntity>() {
			@Override
			public UserEntity run() {
				// make sure we are creating user with unique email
				uniqueIndexManager.saveUniqueIndexOwner(UniqueProps.user_email, user.getEmail(), userKey);
				ofy().save().entity(user).now();
				return user;
			}
		});
	}


	@Override
    @AllowedForOwner
    @AllowedForAdmins
    public UserEntity updateUser(final UpdateUserDto cmd) {
        checkNotNull(cmd, "User to change must not be empty!");
        checkNotNull(cmd.getUserId(), "User id must not be empty!");
        log.debug(format("User [%d] wants to update user [%d]", cc().getLoggedUserId(), cmd.getUserId()));

        return ofy().transact(new Work<UserEntity>() {
            @Override
            public UserEntity run() {
                final Key<UserEntity> key = UserEntity.createKey(cmd.getUserId());
                final UserEntity user = ofy().load().key(key).now();
                if (user == null) throw new NotFoundException(key);

                final String origEmail = user.getEmail();

                user.setEmail(cmd.getEmail());
	            user.setFirstName(cmd.getFirstName());
	            user.setLastName(cmd.getLastName());

	            if (cc().isAdmin() && cmd.getRole() != null) {
	            	user.setRole(cmd.getRole());
	            }

                if (!isNullOrEmpty(cmd.getPassword())) {
                    // also regenerate salt
                    user.setPasswordHash(hashPassword(cmd.getPassword()));
                }
                // do we need to release and acquire new unique for changed email?
                if (!Objects.equals(origEmail, user.getEmail())) {
                    uniqueIndexManager.deleteUniqueIndexOwner(UniqueProps.user_email, origEmail);
                    uniqueIndexManager.saveUniqueIndexOwner(UniqueProps.user_email, user.getEmail(), key);
                }
                ofy().save().entity(user).now();
                return user;
            }
        });
    }


	@Override
	@AllowedForAdmins
	public UserEntity getUser(Long id) {
		return ofy().load().key(UserEntity.createKey(id)).now();
	}

    private String hashPassword(String password) {
        return hashPassword(password, BCrypt.gensalt(10));
    }

    private String hashPassword(String password, String salt) {
        checkArgument(!isNullOrEmpty(password), "Password cannot be blank");
        checkArgument(!isNullOrEmpty(salt), "Salt cannot be empty");
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public UserEntity useAuthToken(String token) {
        return authTokenManager.useToken(token);
    }

    @Override
    public LoginResult login(String email, String password, boolean admin) {
        log.debug(format("Attempting to login user with email: %s and password: %s", email, protectPwd(password)));
        if (isNullOrEmpty(email) || isNullOrEmpty(password)) {
            log.debug("User could not be authenticated, credentials empty");
            return new InvalidCredentialsLoginResult();
        }
        
        final UserEntity found = ofy().load().type(UserEntity.class).filter("email", email).first().now();
        String passwordHash = null;

        // check that user is admin, if it is required
        if (found == null) {
            log.debug(format("No such user with email %s found", email));
        } else if (admin && (found.getRole() == null || !found.getRole().isAdmin())) {
            log.info(format("Login limited to admin only, but %s is is not an admin!", email));
        } else {
            passwordHash = found.getPasswordHash();
        }
        
        if (isNullOrEmpty(passwordHash)) {
            // do the hashing work to mask the timing attack
            hashPassword("aa");
            return new InvalidCredentialsLoginResult();
            
        } else if (!Objects.equals(passwordHash, hashPassword(password, passwordHash))) {
            log.info("User %s attempted to login with invalid password", email);
            return new InvalidCredentialsLoginResult();
        }

        final String token = authTokenManager.newAuthTokenFor(found);

        return new LoginResult(true, found, token);
    }

    @Override
    @AllowedForAuthenticated
    public void logout(String authToken) {
        final Long curUserId = cc().getLoggedUserId();
        final AuthTokenEntity found = authTokenManager.getAuthToken(authToken);
        if (found == null) {
            log.info(format("Auth token [%s] is invalid", authToken));
            return;
        }
        final Long ownerId = keyToId(found.getOwner());
        if (!Objects.equals(curUserId, ownerId)) {
            log.info(format("User [%d] attempted to invalidate token [%s] which is owned by another user [%d]", curUserId, authToken, ownerId));
            return;
        }
        authTokenManager.destroyToken(authToken);
    }

    @Override
    public ListResult<UserEntity> listUsers(ListUsersFilter filter) {
        final Query<UserEntity> query = ofy().load().type(UserEntity.class);
        final List<UserEntity> result = filter.query(query).list();

        return filter.result(result);
    }

    @Override
    @AllowedForAdmins
    @AllowedForTrusted
    public UserEntity makeSuperUser(Long userId) {
        checkNotNull(userId, "User ID must not be empty!");
        final Key<UserEntity> key = UserEntity.createKey(userId);
        return ofy().transact(new Work<UserEntity>() {
            @Override
            public UserEntity run() {
                final UserEntity user = ofy().load().key(key).now();
                if (user == null) {
                    throw new NotFoundException(key);
                }
                user.setRole(Role.ADMIN);
                ofy().save().entity(user).now();
                return user;
            }
        });
    }

    private String protectPwd(String password) {
        if (isNullOrEmpty(password)) return password;
        return password.replaceAll(".", "*");
    }

    @Inject
    public void setAuthTokenManager(AuthTokenManager<UserEntity> authTokenManager) {
        authTokenManager.setTokenValidDuration(Duration.standardDays(60L));
        this.authTokenManager = authTokenManager;
    }

    @Inject
    public void setUniqueIndexManager(UniqueIndexManager uniqueIndexManager) {
        this.uniqueIndexManager = uniqueIndexManager;
    }

    @Inject
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
}
