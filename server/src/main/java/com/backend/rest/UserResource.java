package com.backend.rest;

import com.backend.domain.UserEntity;
import com.backend.domain.dto.UserDto;
import com.backend.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST endpoints having something to do with user resource
 */
@Path("/v1/users")
public class UserResource extends BaseResource {

    private UserService userService;

    @POST
    public UserEntity register(UserDto user) {
        return userService.createUser(user);
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
