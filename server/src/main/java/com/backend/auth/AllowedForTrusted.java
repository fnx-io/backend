package com.backend.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows calls authenticated as Trusted
 */
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD})
public @interface AllowedForTrusted {
}
