package io.fnx.backend.auth;

import com.google.inject.servlet.RequestScoped;
import io.fnx.backend.domain.Role;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.tools.auth.PrincipalRole;
import io.fnx.backend.tools.hydration.HydrationContext;

import java.util.Collections;
import java.util.List;

/**
 * Request scoped holder for currently authenticated user
 */
@RequestScoped
public class CallContext implements HydrationContext {

    private UserEntity loggedUser;

    /**
     * When context is trusted, it might not be authenticated by
     * an user, but might be authenticated via Appengine security rules
     */
    private boolean trusted = false;

    public UserEntity getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserEntity loggedUser) {
        this.loggedUser = loggedUser;
    }

    public Long getLoggedUserId() {
        if (logged()) {
            return loggedUser.getId();
        } else {
            return null;
        }
    }

    public boolean logged() {
        return loggedUser != null && loggedUser.getId() != null;
    }

    public List<? extends PrincipalRole> getCurrentRoles() {
        if (loggedUser == null || loggedUser.getRoles() == null) {
            return Collections.singletonList(Role.ANONYMOUS);
        } else {
            return loggedUser.getRoles();
        }
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public boolean isAdmin() {
        return loggedUser != null && loggedUser.hasAdminRole();
    }
}
