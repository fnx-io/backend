package io.fnx.backend.rest;

import io.fnx.backend.auth.guards.AllAllowed;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.domain.dto.user.UpdateUserDto;
import io.fnx.backend.domain.dto.user.UserDto;
import io.fnx.backend.domain.filter.user.ListUsersFilter;
import io.fnx.backend.service.ListResult;
import io.fnx.backend.service.UserService;
import io.fnx.backend.tools.authorization.AllowedForAdmins;
import io.fnx.backend.tools.authorization.AllowedForOwner;

import javax.inject.Inject;
import javax.ws.rs.*;

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
    @AllowedForAdmins
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
    @AllowedForAdmins
    public UserEntity create(UserDto user) {
        return userService.createUser(user);
    }

	/**
	 * Registers new user. May fail, if some user already registered with same email address.
	 *
	 * @param user details about the user to be created
	 * @return Newly created user when the registration was successful (contains ID for the user).
	 */
	@POST
	@Path("/register")
    @AllAllowed
	public UserEntity register(UserDto user) {
		return userService.registerUser(user);
	}


    @PUT
    @Path("/{id}")
    @AllowedForAdmins
    @AllowedForOwner
    public UserEntity update(@PathParam("id") Long id, UpdateUserDto cmd) {
        cmd.setUserId(id);
        return userService.updateUser(cmd);
    }

	@GET
	@Path("/{id}")
    @AllowedForAdmins
	public UserEntity getUser(@PathParam("id") Long id) {
		return userService.getUser(id);
	}


    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
