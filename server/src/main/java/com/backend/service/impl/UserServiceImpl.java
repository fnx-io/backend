package com.backend.service.impl;

import com.backend.domain.Role;
import com.backend.domain.UniqueProps;
import com.backend.domain.UserEntity;
import com.backend.domain.dto.UserDto;
import com.backend.service.BaseService;
import com.backend.service.UserService;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import io.fnx.backend.manager.AuthTokenManager;
import io.fnx.backend.manager.UniqueIndexManager;
import org.joda.time.Duration;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class UserServiceImpl extends BaseService implements UserService {

    private AuthTokenManager<UserEntity> authTokenManager;
    private UniqueIndexManager uniqueIndexManager;

    @Override
    public UserEntity createUser(UserDto cmd) {
        checkNotNull(cmd, "User must not be null");
        final Key<UserEntity> userKey = ofy().factory().allocateId(UserEntity.class);
        final UserEntity user = new UserEntity();
        user.setId(userKey.getId());
        user.setEmail(cmd.getEmail());
        user.setName(cmd.getName());
        user.setPasswordHash(hashPassword(cmd.getPassword()));
        user.setRole(Role.USER);

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

    @Inject
    public void setAuthTokenManager(AuthTokenManager<UserEntity> authTokenManager) {
        authTokenManager.setTokenValidDuration(Duration.standardDays(60L));
        this.authTokenManager = authTokenManager;
    }

    @Inject
    public void setUniqueIndexManager(UniqueIndexManager uniqueIndexManager) {
        this.uniqueIndexManager = uniqueIndexManager;
    }
}
