package io.fnx.backend.auth;

import io.fnx.backend.domain.Role;
import io.fnx.backend.domain.UserEntity;
import com.google.inject.servlet.RequestScoped;

/**
 * Request scoped holder for currently authenticated user
 */
@RequestScoped
public class CallContext {

    private UserEntity loggedUser;

    /**
     * When context is trusted, it might not be authenticated by
     * an user, but might be authenticated via Appengine security rules
     */
    private boolean trusted = false;

    public UserEntity getLoggedUser() {
        return loggedUser;
    }

    public Long getLoggedUserId() {
        if (logged()) {
            return loggedUser.getId();
        } else {
            return null;
        }
    }

    public void setLoggedUser(UserEntity loggedUser) {
        this.loggedUser = loggedUser;
    }

    public boolean logged() {
        return loggedUser != null && loggedUser.getId() != null;
    }

    public Role getCurrentRole() {
        if (loggedUser == null || loggedUser.getRole() == null) {
            return Role.ANONYMOUS;
        } else {
            return loggedUser.getRole();
        }
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public boolean isTrusted() {
        return trusted;
    }
}
