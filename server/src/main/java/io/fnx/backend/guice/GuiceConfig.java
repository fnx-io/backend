package io.fnx.backend.guice;

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

	private static Injector INSTANCE = null;

    @Override
    protected Injector getInjector() {
        log.info("Initializing Guice injector");

        // This is very dirty, but we need it
	    // because some libraries are just incompatible with DI
        if (INSTANCE == null) {
	        INSTANCE = Guice.createInjector(
			        new FnxGaeToolsModule(),
			        new UtilModule(),
			        new ObjectifyModule(),
			        new RestModule(),
					new ResourceModule(),
					new SwaggerModule(),
					new AuthorizationModule(),
			        new ServiceModule(),
			        new WebModule()
	        );
        }
        return INSTANCE;
    }

	public static Injector getInjectorInstance() {
    	return INSTANCE;
	}

}
