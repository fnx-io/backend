package io.fnx.backend.rest.jersey;

import com.google.common.collect.Sets;
import io.fnx.backend.rest.*;
import io.fnx.backend.rest.secure.SystemResource;

import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configures all classes for our Jersey app
 */
public class JerseyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final HashSet<Class<?>> classes = Sets.newHashSet();

        classes.addAll(resources);

        classes.add(ExceptionMapper.class);
        // json reading/writing
        classes.add(JsonWriter.class);
        classes.add(JsonReader.class);

        return classes;
    }

    /**
     * These are all REST Endpoints (resources) for our API
     */
    public static final List<Class<?>> resources = Arrays.asList(new Class<?>[]{
            UserResource.class,
            FileResource.class,
            MonitoringResource.class,
		    CmsArticleResource.class,
		    ConfigResource.class,
            SystemResource.class,
            SessionResource.class
    });
}
