package com.backend.guice;

import com.backend.service.UserService;
import com.backend.service.impl.UserServiceImpl;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Service layer module
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
    }
}
