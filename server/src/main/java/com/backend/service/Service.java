package com.backend.service;

import java.lang.annotation.*;

/**
 * Denotes something resembling a service. Services might be guarded
 * by some interceptors.
 *
 * @see com.backend.guice.validation.ValidatorInterceptor
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
