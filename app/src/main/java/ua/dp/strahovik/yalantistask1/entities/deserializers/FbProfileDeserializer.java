package ua.dp.strahovik.yalantistask1.entities.deserializers;


import android.net.Uri;

import com.facebook.Profile;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FbProfileDeserializer implements JsonDeserializer<Profile> {

    @Override
    public Profile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject profileObj = json.getAsJsonObject();
        return new Profile(
                profileObj.get("id").getAsString(),
                profileObj.has("first_name") ? profileObj.get("first_name").getAsString() : null,
                profileObj.has("middle_name") ? profileObj.get("middle_name").getAsString() : null,
                profileObj.has("last_name") ? profileObj.get("last_name").getAsString() : null,
                profileObj.has("name") ? profileObj.get("name").getAsString() : null,
                profileObj.has("link") ? Uri.parse(profileObj.get("link").getAsString()) : null
        );
    }
}