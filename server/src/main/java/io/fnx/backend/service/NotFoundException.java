package io.fnx.backend.service;

import com.google.common.base.Preconditions;
import com.googlecode.objectify.Key;

import static java.lang.String.format;

public class NotFoundException extends RuntimeException {

    public final String entity;

    /**
     * its either numerical entity id, or string entity name,
     * it depends on what the underlying key was
     */
    public final Object id;

    public final Key<?> k;

    public NotFoundException(Key<?> k) {
        super(createMessage(k));
        this.entity = k.getKind();
        this.id = keyId(k);
        this.k = k;
    }

    public static String createMessage(Key<?> k) {
        Preconditions.checkNotNull(k, "Entity key must not be null!");
        return format("Requested %s:%s could not be found", k.getKind(), keyId(k));
    }

    private static Object keyId(Key<?> k) {
        if (k.getId() != 0) {
            return k.getId();
        } else {
            return k.getName();
        }
    }
}
