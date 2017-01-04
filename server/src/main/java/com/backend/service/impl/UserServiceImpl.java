package com.backend.service.impl;

import com.backend.domain.UserEntity;
import com.backend.service.BaseService;
import com.backend.service.UserService;
import io.fnx.backend.manager.AuthTokenManager;
import org.joda.time.Duration;

import javax.inject.Inject;

public class UserServiceImpl extends BaseService implements UserService {

    private AuthTokenManager<UserEntity> authTokenManager;

    @Override
    public UserEntity useAuthToken(String token) {
        return authTokenManager.useToken(token);
    }

    @Inject
    public void setAuthTokenManager(AuthTokenManager<UserEntity> authTokenManager) {
        authTokenManager.setTokenValidDuration(Duration.standardDays(60L));
        this.authTokenManager = authTokenManager;
    }
}
