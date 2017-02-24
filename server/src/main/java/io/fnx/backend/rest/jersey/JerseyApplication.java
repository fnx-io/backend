package io.fnx.backend.rest.jersey;

import io.fnx.backend.rest.*;
import io.fnx.backend.rest.secure.SystemResource;
import io.fnx.backend.rest.secure.tasks.DelayedTasksResource;
import com.google.common.collect.Sets;

import javax.ws.rs.core.Application;
import java.util.*;

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
    private static final List<Class<?>> resources = Arrays.asList(new Class<?>[]{
            UserResource.class,
            FileResource.class,
            MonitoringResource.class,
            AuditLogEventResource.class,
		    CmsArticleResource.class,

            SystemResource.class,
            DelayedTasksResource.class
    });
}
