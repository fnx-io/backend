package com.backend.rest.jersey;

import com.backend.guice.validation.ValidationException;
import com.backend.util.Constants;
import com.google.common.collect.Maps;
import com.sun.jersey.api.NotFoundException;
import io.fnx.backend.manager.UniqueViolationException;
import io.fnx.backend.tools.authorization.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.*;

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
        } else if (t instanceof ValidationException) {
            final ErrorResponse err = renderValidationError((ValidationException) t);
            return unprocessableEntity(err);
        } else if (t instanceof UniqueViolationException) {
            return unprocessableEntity(renderUniqueViolationException((UniqueViolationException) t));
        } else if (t instanceof com.backend.service.NotFoundException) {
            return unprocessableEntity(entityNotFound((com.backend.service.NotFoundException) t));
        } else if (t instanceof PermissionDeniedException) {
            return Response.status(Response.Status.FORBIDDEN).
                    entity(new ErrorResponse("PermissionDenied", t.getMessage(), null)).
                    build();
        } else {
            log.warn(t.getMessage(), t);
            return Response.status(500).entity(t.getMessage()).build();
        }
    }

    private ErrorResponse entityNotFound(com.backend.service.NotFoundException t) {
        Map<String, Object> details = Maps.newHashMap();
        details.put("entity", t.entity);
        details.put("id", t.id);
        return new ErrorResponse("NotFound", t.getMessage(), Collections.singleton(details));
    }

    private ErrorResponse renderUniqueViolationException(UniqueViolationException t) {
        return new ErrorResponse("UniqueValueViolation", t.getMessage(), null);
    }

    public static ErrorResponse renderValidationError(ValidationException e) {
        final List<Map<String, Object>> details = new ArrayList<>();
        for (ConstraintViolation<Object> c : e.getResult()) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("path", c.getPropertyPath().toString());
            obj.put("messageTemplate", c.getMessageTemplate());
            obj.put("message", c.getMessage());
            details.add(obj);
        }
        return new ErrorResponse("ValidationError", null, details);
    }

    private Response unprocessableEntity(ErrorResponse err) {
        return Response.status(Constants.STATUS_VALIDATION_FAILED).entity(err).build();
    }

    public static class ErrorResponse {
        public final boolean error = true;
        public final String type;
        public final String description;
        public final Collection<Map<String, Object>> details;

        public ErrorResponse(String type, String description, Collection<Map<String, Object>> details) {
            this.type = type;
            this.description = description;
            this.details = details;
        }
    }
}
