package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import io.fnx.backend.rest.jersey.JerseyApplication;

/**
 * Service layer module
 */
public class ResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        for (Class<?> c : JerseyApplication.resources) {
            bind(c);
        }
    }


}
