package com.backend.auth;

import com.backend.domain.Role;
import com.backend.domain.UserEntity;
import com.google.inject.servlet.RequestScoped;

/**
 * Request scoped holder for currently authenticated user
 */
@RequestScoped
public class CallContext {

    private UserEntity loggedUser;

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
}
