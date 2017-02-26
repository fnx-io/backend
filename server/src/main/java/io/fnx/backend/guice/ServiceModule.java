package io.fnx.backend.guice;

import io.fnx.backend.auth.AllowedForRoleAuthorizationGuard;
import io.fnx.backend.auth.AllowedForTrustedAuthorizationGuard;
import io.fnx.backend.guice.validation.ValidatorInterceptor;
import io.fnx.backend.guice.validation.ValidatorProvider;
import io.fnx.backend.queue.QueueProvider;
import io.fnx.backend.queue.QueueProviderFactory;
import io.fnx.backend.queue.TaskSubmitterFactory;
import io.fnx.backend.service.*;
import io.fnx.backend.service.impl.CmsArticleServiceImpl;
import io.fnx.backend.service.impl.DelayedTaskServiceImpl;
import io.fnx.backend.service.impl.FileServiceImpl;
import io.fnx.backend.service.impl.UserServiceImpl;
import io.fnx.backend.tools.hydration.Hydrator;
import io.fnx.backend.util.conf.AppConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import io.fnx.backend.tools.authorization.*;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Service layer module
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
        bind(FileService.class).to(FileServiceImpl.class).in(Singleton.class);
	    bind(CmsArticleService.class).to(CmsArticleServiceImpl.class).in(Singleton.class);
        bind(DelayedTaskService.class).to(DelayedTaskServiceImpl.class).in(Singleton.class);

	    bind(Hydrator.class).in(Singleton.class);

        bind(AppConfiguration.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(QueueProvider.class, QueueProvider.class)
                .build(QueueProviderFactory.class));
        bind(TaskSubmitterFactory.class).in(Singleton.class);

        {
            bind(javax.validation.Validator.class).toProvider(ValidatorProvider.class).in(Singleton.class);
            ValidatorInterceptor validatorInterceptor = new ValidatorInterceptor();
            requestInjection(validatorInterceptor);
            bindInterceptor(Matchers.annotatedWith(Service.class), new ValidateMethodMatcher(), validatorInterceptor);
        }

        {
            final LoggedPrincipalProvider loggedUserProvider = new LoggedPrincipalProvider();
            requestInjection(loggedUserProvider);
            final AuthorizationInterceptor fnxAuthorizationInterceptor = new AuthorizationInterceptor(loggedUserProvider);
            fnxAuthorizationInterceptor.setGuards(createAuthorizationGuards());
            requestInjection(fnxAuthorizationInterceptor);
            bindInterceptor(Matchers.annotatedWith(Service.class), Matchers.any(), fnxAuthorizationInterceptor);
        }
    }

    private AuthorizationGuard[] createAuthorizationGuards() {
        final ArrayList<AuthorizationGuard> guards = new ArrayList<>();
        guards.add(new AllowedForAuthenticatedAuthorizationGuard());
        guards.add(new AllowedForAdminsAuthorizationGuard());
        final AllowedForRoleAuthorizationGuard roleGuard = new AllowedForRoleAuthorizationGuard();
        guards.add(roleGuard);
        final AllowedForOwnerAuthorizationGuard ownerGuard = new AllowedForOwnerAuthorizationGuard();
        requestInjection(ownerGuard);
        guards.add(ownerGuard);

        final AllowedForTrustedAuthorizationGuard trustedGuard = new AllowedForTrustedAuthorizationGuard();
        requestInjection(trustedGuard);
        guards.add(trustedGuard);

        return guards.toArray(new AuthorizationGuard[guards.size()]);
    }

    private static class ValidateMethodMatcher implements Matcher<Method> {

        static Set<String> DONT_VALIDATE = new HashSet<>();
        static {
            DONT_VALIDATE.add("hashCode");
            DONT_VALIDATE.add("equals");
            DONT_VALIDATE.add("clone");
            DONT_VALIDATE.add("toString");
            DONT_VALIDATE.add("finalize");
        }

        @Override
        public boolean matches(Method method) {
            if (DONT_VALIDATE.contains(method.getName())) {
                return false;
            }
            if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
                return false;
            }
            return !method.isSynthetic() && method.getAnnotation(DontValidate.class) == null;
        }

        @Override
        public Matcher<Method> and(Matcher<? super Method> other) {
            throw new IllegalStateException("Not implemented");
        }

        @Override
        public Matcher<Method> or(Matcher<? super Method> other) {
            throw new IllegalStateException("Not implemented");
        }
    }
}
