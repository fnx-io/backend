package com.backend.service;

import com.backend.domain.UserEntity;
import com.backend.domain.dto.UserDto;

/**
 * Service responsible for handling users
 */
public interface UserService {

    UserEntity createUser(UserDto cmd);

    UserEntity useAuthToken(String token);
}
