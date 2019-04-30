package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import io.fnx.backend.rest.*;
import io.fnx.backend.rest.jersey.JerseyApplication;
import io.fnx.backend.rest.secure.SystemResource;
import io.fnx.backend.service.*;
import io.fnx.backend.service.impl.*;

import javax.inject.Singleton;

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
