package com.backend.rest.jersey;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CorsHeadersFilter implements ContainerResponseFilter {

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        response.getHttpHeaders().putSingle("Access-Control-Allow-Origin", request.getHeaderValue("Origin"));
        response.getHttpHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        response.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Authorization, X-Filename, Origin, X-Requested-With, Content-Type, Accept");
        response.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        return response;
    }
}
