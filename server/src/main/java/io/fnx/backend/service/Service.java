package io.fnx.backend.service;

import io.fnx.backend.guice.validation.ValidatorInterceptor;

import java.lang.annotation.*;

/**
 * Denotes something resembling a service. Services might be guarded
 * by some interceptors.
 *
 * @see ValidatorInterceptor
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
