package io.fnx.backend.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker which will force skipping validation of arguments of given method
 */
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD})
public @interface DontValidate { }
