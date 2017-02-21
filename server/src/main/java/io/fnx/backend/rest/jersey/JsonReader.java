package io.fnx.backend.rest.jersey;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.fnx.backend.gson.GsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Can deserialize message body from JSON to objects using configured {@link Gson}
 *
 * @see GsonProvider
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JsonReader implements MessageBodyReader<Object> {

    @Inject
    private Gson gson;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return gson.fromJson(new InputStreamReader(entityStream, "UTF-8"), genericType);
    }
}
