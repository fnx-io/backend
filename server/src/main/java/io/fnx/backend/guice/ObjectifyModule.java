package io.fnx.backend.guice;

import io.fnx.backend.domain.CmsArticleEntity;
import io.fnx.backend.domain.FileEntity;
import io.fnx.backend.domain.UserEntity;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import io.fnx.backend.domain.eventlog.AuditLogEventEntity;

import javax.inject.Singleton;

import static com.googlecode.objectify.ObjectifyService.register;

/**
 * Takes care of things needed to be done with objectify
 */
public class ObjectifyModule extends ServletModule {

    static {
        JodaTimeTranslators.add(ObjectifyService.factory());
        register(UserEntity.class);
        register(FileEntity.class);
        register(AuditLogEventEntity.class);
	    register(CmsArticleEntity.class);
    }

    @Override
    protected void configureServlets() {
        bind(ObjectifyFilter.class).in(Singleton.class);
        filter("/*").through(ObjectifyFilter.class);
    }
}
