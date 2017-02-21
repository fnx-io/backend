package io.fnx.backend.guice.validation;

import io.fnx.backend.service.DontValidate;
import io.fnx.backend.service.Validate;
import com.google.inject.Singleton;
import com.googlecode.objectify.annotation.Entity;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.Set;

@Singleton
public class ValidatorInterceptor implements MethodInterceptor {

    private Logger log = LoggerFactory.getLogger(ValidatorInterceptor.class);

    @Inject
    private javax.validation.Validator validator;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method m = invocation.getMethod();
        if (m.getAnnotation(DontValidate.class) != null) return invocation.proceed();

        // We always want to validate default group.
        // In default group end all constraints with no group explicitly specified
        Class<?>[] validationGroups = new Class<?>[]{Default.class};
        final Validate validateAnn = m.getAnnotation(Validate.class);
        if (validateAnn != null) {
            final Class<?>[] groups = validateAnn.groups();
            if (groups.length > 0) {
                final Class<?>[] mergedGroups = new Class<?>[groups.length + validationGroups.length];
                System.arraycopy(validationGroups, 0, mergedGroups, 0, validationGroups.length);
                System.arraycopy(groups, 0, mergedGroups, validationGroups.length, groups.length);
                validationGroups = mergedGroups;
            }
        }
        for (Object o : invocation.getArguments()) {
            if (o == null) continue;
            final Class<?> clazz = o.getClass();
            if (clazz.getAnnotation(Entity.class) != null || clazz.getAnnotation(Validate.class) != null) {

                Set<ConstraintViolation<Object>> result = validator.validate(o, validationGroups);
                if (result != null && !result.isEmpty()) {
                    throw new ValidationException(clazz, m, result);
                }
            }

        }
        return invocation.proceed();
    }

}
