package io.fnx.backend.gson;

import io.fnx.backend.domain.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Excludes fields annotated by {@link JsonIgnore}
 */
public class AnnotationBasedExclusion implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(JsonIgnore.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(JsonIgnore.class) != null;
    }
}
