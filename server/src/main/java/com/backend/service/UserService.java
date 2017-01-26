package com.backend.service;

import com.backend.domain.UserEntity;
import com.backend.domain.dto.user.UpdateUserDto;
import com.backend.domain.dto.user.UserDto;
import com.backend.domain.dto.login.LoginResult;
import com.backend.domain.filter.user.ListUsersFilter;

/**
 * Service responsible for handling users
 */
public interface UserService {

    UserEntity createUser(UserDto cmd);

    UserEntity updateUser(UpdateUserDto cmd);

    UserEntity useAuthToken(String token);

    LoginResult login(String email, String password);

    void logout(String authToken);

    ListResult<UserEntity> listUsers(ListUsersFilter filter);

    UserEntity makeSuperUser(Long userId);
}
