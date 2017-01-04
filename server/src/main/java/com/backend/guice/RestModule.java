package com.backend.guice;

import com.backend.gson.GsonProvider;
import com.backend.rest.jersey.CorsHeadersFilter;
import com.backend.rest.jersey.JerseyApplication;
import com.backend.rest.jersey.JerseyAuthFilter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.inject.Singleton;
import java.util.*;

/**
 * Guice module which configures Jersey to server our Rest endpoints
 */
public class RestModule extends ServletModule {

    @Override
    protected void configureServlets() {

        // provider Gson as early as possible
        bind(Gson.class).toProvider(GsonProvider.class).in(Singleton.class);

        final Map<String, String> params = new HashMap<String, String>();
        params.put("javax.ws.rs.Application", JerseyApplication.class.getName());
        params.put("com.sun.jersey.config.feature.DisableWADL", "true");
        params.put("com.sun.jersey.spi.container.ContainerRequestFilters", joinParams(requestFilters()));
        params.put("com.sun.jersey.spi.container.ContainerResponseFilters", joinParams(responseFilters()));

        serve("/api/*").with(GuiceContainer.class, params);
    }

    public static Collection<Class<?>> requestFilters() {
        List<Class<?>> f = Lists.newLinkedList();
        f.add(JerseyAuthFilter.class);

        return Collections.unmodifiableList(f);
    }

    public static Collection<Class<?>> responseFilters() {
        List<Class<?>> f = Lists.newLinkedList();
        f.add(CorsHeadersFilter.class);

        return Collections.unmodifiableList(f);
    }

    public static String joinParams(Iterable<?> col) {
        if (col == null) return null;
        return Joiner.on(",").join(col);
    }

}
