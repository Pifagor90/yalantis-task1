package ua.dp.strahovik.yalantistask1.entities.deserializers;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ua.dp.strahovik.yalantistask1.entities.EventState;

public class EventStateDeserializer implements JsonDeserializer<EventState> {

    @Override
    public EventState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject eventStateObj = json.getAsJsonObject();
        EventState eventState = new EventState();
        eventState.setState(eventStateObj.get("name").getAsString());
        eventState.setStateId(eventStateObj.get("id").getAsInt());
        return eventState;
    }
}
