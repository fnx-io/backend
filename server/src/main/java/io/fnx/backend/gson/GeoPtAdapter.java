package io.fnx.backend.gson;

import com.google.appengine.api.datastore.GeoPt;
import com.google.gson.*;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class GeoPtAdapter implements JsonSerializer<GeoPt>, JsonDeserializer<GeoPt> {

    public static final DateTimeFormatter API_DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();

    @Override
    public GeoPt deserialize(final JsonElement json,
                             final Type typeOfT,
                             final JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        } if (json.isJsonNull()) {
            return null;
        } else {
            return new GeoPt(json.getAsJsonArray().get(0).getAsFloat(), json.getAsJsonArray().get(1).getAsFloat());
        }
    }
	
    @Override
    public JsonElement serialize(final GeoPt src,
                                 final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        a.add(src.getLatitude());
	    a.add(src.getLongitude());
        return a;
    }
}
