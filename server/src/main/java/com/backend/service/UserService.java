package com.backend.service;

import com.backend.domain.UserEntity;
import com.backend.domain.dto.UserDto;
import com.backend.domain.dto.login.LoginResult;
import com.backend.domain.filter.user.ListUsersFilter;

/**
 * Service responsible for handling users
 */
public interface UserService {

    UserEntity createUser(UserDto cmd);

    UserEntity useAuthToken(String token);

    LoginResult login(String email, String password);

    void logout(String authToken);

    ListResult<UserEntity> listUsers(ListUsersFilter filter);
}
