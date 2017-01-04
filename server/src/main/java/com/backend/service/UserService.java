package com.backend.service;

import com.backend.domain.UserEntity;

/**
 * Service responsible for handling users
 */
public interface UserService {


    UserEntity useAuthToken(String token);
}
