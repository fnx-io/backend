package com.backend.guice;

import com.backend.guice.validation.ValidatorInterceptor;
import com.backend.guice.validation.ValidatorProvider;
import com.backend.service.DontValidate;
import com.backend.service.Service;
import com.backend.service.UserService;
import com.backend.service.impl.UserServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Service layer module
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
        {
            bind(javax.validation.Validator.class).toProvider(ValidatorProvider.class).in(Singleton.class);
            ValidatorInterceptor validatorInterceptor = new ValidatorInterceptor();
            requestInjection(validatorInterceptor);
            bindInterceptor(Matchers.annotatedWith(Service.class), new ValidateMethodMatcher(), validatorInterceptor);
        }
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
