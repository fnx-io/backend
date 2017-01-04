package com.backend.rest.jersey;

import com.backend.rest.MonitoringResource;
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

    private static final List<Class<?>> resources = Arrays.asList(new Class<?>[]{
            MonitoringResource.class
    });
}
