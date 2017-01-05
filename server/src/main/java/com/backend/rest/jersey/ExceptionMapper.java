package com.backend.rest.jersey;

import com.backend.guice.validation.ValidationException;
import com.backend.util.Constants;
import com.sun.jersey.api.NotFoundException;
import io.fnx.backend.manager.UniqueViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        } else {
            log.warn(t.getMessage(), t);
            return Response.status(500).entity(t.getMessage()).build();
        }
    }

    private ErrorResponse renderUniqueViolationException(UniqueViolationException t) {
        return new ErrorResponse("UniqueValueViolation", t.getMessage(), null);
    }

    public static ErrorResponse renderValidationError(ValidationException e) {
        final List<Map<String, String>> details = new ArrayList<>();
        for (ConstraintViolation<Object> c : e.getResult()) {
            Map<String, String> obj = new HashMap<>();
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
        public final List<Map<String, String>> details;

        public ErrorResponse(String type, String description, List<Map<String, String>> details) {
            this.type = type;
            this.description = description;
            this.details = details;
        }
    }
}
