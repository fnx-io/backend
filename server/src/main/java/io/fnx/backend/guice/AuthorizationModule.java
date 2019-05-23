package io.fnx.backend.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import io.fnx.backend.auth.guards.AllowedForRolesAuthorizationGuardImpl;
import io.fnx.backend.auth.guards.AllowedForTrustedAuthorizationGuard;
import io.fnx.backend.queue.QueueProvider;
import io.fnx.backend.queue.QueueProviderFactory;
import io.fnx.backend.queue.TaskSubmitterFactory;
import io.fnx.backend.rest.BaseResource;
import io.fnx.backend.service.DontValidate;
import io.fnx.backend.tools.authorization.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service layer module
 */
public class AuthorizationModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(QueueProvider.class, QueueProvider.class).build(QueueProviderFactory.class));
        bind(TaskSubmitterFactory.class).in(Singleton.class);
/*
        {
            bind(javax.validation.Validator.class).toProvider(ValidatorProvider.class).in(Singleton.class);
            ValidatorInterceptor validatorInterceptor = new ValidatorInterceptor();
            requestInjection(validatorInterceptor);
            bindInterceptor(Matchers.annotatedWith(Service.class), new ValidateMethodMatcher(), validatorInterceptor);
        }
*/
        {
            final LoggedPrincipalProvider loggedUserProvider = new LoggedPrincipalProvider();
            requestInjection(loggedUserProvider);
            final AuthorizationInterceptor fnxAuthorizationInterceptor = new AuthorizationInterceptor(loggedUserProvider);
            fnxAuthorizationInterceptor.setGuards(createAuthorizationGuards());
            requestInjection(fnxAuthorizationInterceptor);
            bindInterceptor(Matchers.subclassesOf(BaseResource.class), new ResourceMethodMatcher(), fnxAuthorizationInterceptor);
        }
    }

    private AuthorizationGuard[] createAuthorizationGuards() {
        final ArrayList<AuthorizationGuard> guards = new ArrayList<>();
        guards.add(new AllAllowedAuthorizationGuard());
        guards.add(new AllowedForAuthenticatedAuthorizationGuard());
        guards.add(new AllowedForAdminsAuthorizationGuard());
        final AllowedForRolesAuthorizationGuardImpl roleGuard = new AllowedForRolesAuthorizationGuardImpl();
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
            /*if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
                return false;
            }*/
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

    private static class ResourceMethodMatcher implements Matcher<Method> {

        static Set<String> DONT_VALIDATE = new HashSet<>();
        static {
            DONT_VALIDATE.add("hashCode");
            DONT_VALIDATE.add("equals");
            DONT_VALIDATE.add("clone");
            DONT_VALIDATE.add("toString");
            DONT_VALIDATE.add("finalize");
        }

        static List<Class<? extends Annotation>> EXCLUDED_ANNOTATIONS = new ArrayList<>();
        static {
            EXCLUDED_ANNOTATIONS.add(Inject.class);
            EXCLUDED_ANNOTATIONS.add(DontValidate.class);
        }

        @Override
        public boolean matches(Method method) {
            if (DONT_VALIDATE.contains(method.getName())) {
                return false;
            }
            if (!Modifier.isPublic(method.getModifiers())) {
                return false;
            }
            if (method.getAnnotations() != null) {
                for (Class claz : EXCLUDED_ANNOTATIONS) {
                    if (method.getAnnotation(claz) != null) return false;
                }
            }
            /*if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
                return false;
            }*/
            return !method.isSynthetic();
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
