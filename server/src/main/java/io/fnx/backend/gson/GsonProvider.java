package io.fnx.backend.gson;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;

import javax.inject.Provider;
import java.util.Collection;

/**
 * Provider for fully configured Gson instance
 */
public class GsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        final GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        builder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());

        registerKeyTypeAdapters(builder);

        builder.addSerializationExclusionStrategy(new AnnotationBasedExclusion());
        return builder.create();
    }

    public static void registerKeyTypeAdapters(GsonBuilder builder) {
        for (SelfRegisteringAdapter<?> adapter : selfRegisteringTypeAdapters()) {
            adapter.registerTypeAdapter(builder);
        }
    }

    public static Collection<SelfRegisteringAdapter<?>> selfRegisteringTypeAdapters() {
        return Lists.newLinkedList();
    }
}
