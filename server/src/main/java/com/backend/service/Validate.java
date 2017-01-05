package com.backend.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes for validation in our {@link ValidatorInterceptor}
 *
 * @author Jiri Zuna (jiri@zunovi.cz)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Validate {

    /**
     * Specify which groups to validate
     * @return all groups for which to run validations
     */
    Class<?>[] groups() default {};
}
