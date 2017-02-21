package io.fnx.backend.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Annotated classes or attributes won't be serialized by Gson
 *
 * @author Tomas Zverina, zverina@m-atelier.cz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface JsonIgnore { }
