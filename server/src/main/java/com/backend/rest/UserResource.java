package com.backend.rest;

import com.backend.auth.CallContext;
import com.backend.domain.UserEntity;
import com.backend.domain.dto.user.UpdateUserDto;
import com.backend.domain.dto.user.UserDto;
import com.backend.domain.dto.login.LoginResult;
import com.backend.domain.dto.login.UserLoginDto;
import com.backend.domain.filter.user.ListUsersFilter;
import com.backend.service.ListResult;
import com.backend.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * REST endpoints having something to do with user resource
 */
@Path("/v1/users")
public class UserResource extends BaseResource {

    private UserService userService;

    /**
     * Allows to list all users
     * @return single page of users
     */
    @GET
    public ListResult<UserEntity> listUsers() {
        return userService.listUsers(new ListUsersFilter(filterLimits()));
    }

    /**
     * Registers new user. May fail, if some user already registered with same email address.
     *
     * @param user details about the user to be created
     * @return Newly created user when the registration was successful (contains ID for the user).
     */
    @POST
    public UserEntity register(UserDto user) {
        return userService.createUser(user);
    }

    @PUT
    @Path("/{id}")
    public UserEntity update(@PathParam("id") Long id, UpdateUserDto cmd) {
        cmd.setUserId(id);
        return userService.updateUser(cmd);
    }

    /**
     * Tries to login the user with provided credentials. If the user is authenticated,
     * also creates new Authorization token
     *
     * @param login the login details
     * @return status 200 if the login attempt was successful or status 401 when the
     * credentials were missing or invalid
     */
    @POST
    @Path("/session")
    public Response login(UserLoginDto login) {
        final LoginResult result = userService.login(login.getEmail(), login.getPassword());
        if (result.isSuccess()) {
            return ok(result);
        } else {
            return unauthorized();
        }
    }

    /**
     * Returns status 200 response with details about currently logged user.
     * <br>
     * If the Authorization header is missing or invalid (maybe expired) returns response with status 403
     *
     * @return Details about currently logged user
     */
    @GET
    @Path("/session")
    public Response me() {
        final CallContext cc = cc();
        if (cc.logged()) {
            return ok(cc.getLoggedUser());
        } else {
            return forbidden();
        }
    }

    /**
     * Invalidates currently used authorization token. Always succeeds with status 200 even if
     * there were some problems with deactivating token (invalid token, owned by someone else) and it
     * could not be invalidated
     *
     * @param authToken the token to be invalidated
     * @return response with status 200
     */
    @DELETE
    @Path("/session")
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authToken) {
        userService.logout(authToken);
        return ok();
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
