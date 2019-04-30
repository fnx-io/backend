package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.domain.Role;
import io.fnx.backend.tools.hydration.Hydrator;
import io.fnx.backend.util.EnumerationRepository;
import io.fnx.backend.util.MessageAccessor;
import io.fnx.backend.util.conf.BackendConfiguration;
import io.fnx.backend.util.conf.ClientConfiguration;

import javax.inject.Singleton;

/**
 * Service layer module
 */
public class UtilModule extends AbstractModule {

    @Override
    protected void configure() {

        MessageAccessor messageAccessor = new MessageAccessor();
	    bind(MessageAccessor.class).toInstance(messageAccessor);

	    EnumerationRepository erepo = new EnumerationRepository();
	    erepo.setFileCategories(EnumerationRepository.buildEnumerationFromEnum(FileCategory.values(), "fileCategory", messageAccessor));
	    erepo.setRoles(EnumerationRepository.buildEnumerationFromEnum(Role.values(), "role", messageAccessor));
	    // more enums here:

	    bind(EnumerationRepository.class).toInstance(erepo);

		bind(Hydrator.class).in(Singleton.class);

		bind(BackendConfiguration.class).in(Singleton.class);
		bind(ClientConfiguration.class).in(Singleton.class);


	}

}
