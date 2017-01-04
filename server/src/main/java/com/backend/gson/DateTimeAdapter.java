package com.backend.gson;

import com.google.gson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class DateTimeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    public static final DateTimeFormatter API_DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();

    @Override
    public DateTime deserialize(final JsonElement json,
                                final Type typeOfT,
                                final JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        } if (json.isJsonNull()) {
            return null;
        } else {
            return parseDateTime(json);
        }
    }

    public DateTime parseDateTime(JsonElement json) {
        return API_DATE_FORMAT.parseDateTime(json.getAsString()).withZone(DateTimeZone.UTC);
    }

    @Override
    public JsonElement serialize(final DateTime src,
                                 final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        final String date = API_DATE_FORMAT.withZoneUTC().print(src);
        return new JsonPrimitive(date);
    }
}
