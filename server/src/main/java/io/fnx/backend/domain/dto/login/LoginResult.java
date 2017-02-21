package io.fnx.backend.domain.dto.login;

import io.fnx.backend.domain.UserEntity;

public class LoginResult {

    protected final boolean success;
    protected final String token;
    protected final UserEntity user;

    public LoginResult(boolean success, UserEntity user, String token) {
        this.success = success;
        this.user = user;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public UserEntity getUser() {
        return user;
    }
}
