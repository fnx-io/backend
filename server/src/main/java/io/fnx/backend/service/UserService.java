package io.fnx.backend.service;

import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.user.UpdateUserDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.domain.dto.login.LoginResult;
import io.fnx.backend.domain.filter.user.ListUsersFilter;

/**
 * Service responsible for handling users
 */
public interface UserService {

    UserEntity createUser(UserDto cmd);

    UserEntity updateUser(UpdateUserDto cmd);

    UserEntity useAuthToken(String token);

    LoginResult login(String email, String password, boolean admin);

    void logout(String authToken);

    ListResult<UserEntity> listUsers(ListUsersFilter filter);

    UserEntity makeSuperUser(Long userId);

	UserEntity getUser(Long id);

}
