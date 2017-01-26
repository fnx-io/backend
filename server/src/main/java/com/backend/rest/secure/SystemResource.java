package com.backend.rest.secure;

import com.backend.domain.UserEntity;
import com.backend.rest.BaseResource;
import com.backend.service.UserService;

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
