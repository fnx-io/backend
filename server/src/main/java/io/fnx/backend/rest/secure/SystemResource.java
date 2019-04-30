package io.fnx.backend.rest.secure;

import io.fnx.backend.auth.guards.AllowedForTrusted;
import io.fnx.backend.domain.UserEntity;
import io.fnx.backend.rest.BaseResource;
import io.fnx.backend.service.UserService;
import io.fnx.backend.tools.authorization.AllowedForAdmins;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * This resource depends on security rules in web.xml, where the url /api/v1/secure/*
 * must be guarded by admin access to appengine.
 */
@Path("/v1/secure/system")
public class SystemResource extends BaseResource {

    private UserService userService;

    @AllowedForAdmins
    @AllowedForTrusted
    @GET
    @Path("/users/{id}/admin")
    public UserEntity makeSuperUser(@PathParam("id") Long userId) {
        return userService.makeSuperUser(userId);
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
