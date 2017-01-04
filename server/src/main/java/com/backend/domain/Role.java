package com.backend.domain;

import io.fnx.backend.tools.auth.PrincipalRole;

public enum Role implements PrincipalRole {
    ADMIN,
    USER,
    ANONYMOUS;

    @Override
    public boolean isAdmin() {
        return this == ADMIN;
    }

    @Override
    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }
}
