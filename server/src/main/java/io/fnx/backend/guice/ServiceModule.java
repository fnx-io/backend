package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import io.fnx.backend.service.*;
import io.fnx.backend.service.impl.*;

import javax.inject.Singleton;

/**
 * Service layer module
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
        bind(FileService.class).to(FileServiceImpl.class).in(Singleton.class);
        bind(AuditLogManager.class).to(AuditLogManagerImpl.class).in(Singleton.class);
        bind(CmsArticleService.class).to(CmsArticleServiceImpl.class).in(Singleton.class);
        bind(MailService.class).to(MailServiceImpl.class).in(Singleton.class);
    }


}
