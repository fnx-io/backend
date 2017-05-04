package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import io.fnx.backend.domain.FileCategory;
import io.fnx.backend.domain.Role;
import io.fnx.backend.util.EnumerationRepository;
import io.fnx.backend.util.MessageAccessor;

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

    }

}
