package com.backend.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Simple class which can register itself with {@link GsonBuilder}
 * @param <T> type this adapter can handle
 */
public abstract class SelfRegisteringAdapter<T> {

    public abstract TypeToken<T> createToken();

    public GsonBuilder registerTypeAdapter(final GsonBuilder builder) {
        return builder.registerTypeAdapter(createToken().getType(), this);
    }
}
