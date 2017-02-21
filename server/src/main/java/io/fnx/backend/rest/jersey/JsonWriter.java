package io.fnx.backend.rest.jersey;

import io.fnx.backend.util.Constants;
import com.google.gson.Gson;
import com.google.inject.Inject;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Can serialize given objects into JSON messages
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JsonWriter implements MessageBodyWriter<Object> {

    @Inject
    private Gson gson;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        String json = gson.toJson(o);
        entityStream.write(json.getBytes(Constants.CHARSET));
    }
}
