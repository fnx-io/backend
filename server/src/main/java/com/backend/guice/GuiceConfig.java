package com.backend.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import io.fnx.backend.tools.FnxGaeToolsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes guice injector
 */
public class GuiceConfig extends GuiceServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(GuiceConfig.class);

    @Override
    protected Injector getInjector() {
        log.info("Initializing Guice injector");
        return Guice.createInjector(
                new FnxGaeToolsModule(),
                new ObjectifyModule(),
                new RestModule(),
                new ServiceModule()
        );
    }
}
