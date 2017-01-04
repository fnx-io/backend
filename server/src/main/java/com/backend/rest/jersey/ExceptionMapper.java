package com.backend.rest.jersey;

import com.sun.jersey.api.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

import static java.lang.String.format;

/**
 * Handles Exception rendering for Jersey
 */
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(ExceptionMapper.class);

    @Override
    public Response toResponse(Throwable t) {
        if (t instanceof NotFoundException) {
            final NotFoundException nfe = (NotFoundException) t;
            log.debug(format("URI Not-Found: %s", nfe.getNotFoundUri()));
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(500).entity(t.getMessage()).build();
        }
    }
}
