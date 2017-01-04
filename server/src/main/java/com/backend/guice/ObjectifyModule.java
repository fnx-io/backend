package com.backend.guice;

import com.backend.domain.UserEntity;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import javax.inject.Singleton;

import static com.googlecode.objectify.ObjectifyService.register;

/**
 * Takes care of things needed to be done with objectify
 */
public class ObjectifyModule extends ServletModule {

    static {
        JodaTimeTranslators.add(ObjectifyService.factory());
        register(UserEntity.class);
    }

    @Override
    protected void configureServlets() {
        bind(ObjectifyFilter.class).in(Singleton.class);
        filter("/*").through(ObjectifyFilter.class);
    }
}
