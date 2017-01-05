package com.backend.domain.dto.login;

public class InvalidCredentialsLoginResult extends LoginResult {

    public InvalidCredentialsLoginResult() {
        super(false, null, null);
    }
}
