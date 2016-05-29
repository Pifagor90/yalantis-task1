package ua.dp.strahovik.yalantistask1.entities.deserializers;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.getAsJsonPrimitive().getAsLong() == 0) {
            return null;
        }
        return new Date(json.getAsJsonPrimitive().getAsLong());
    }
}
