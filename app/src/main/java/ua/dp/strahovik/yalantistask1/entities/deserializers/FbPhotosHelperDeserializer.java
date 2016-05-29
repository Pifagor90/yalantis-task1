package ua.dp.strahovik.yalantistask1.entities.deserializers;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.dp.strahovik.yalantistask1.entities.helpers.FbPhotosHelper;

public class FbPhotosHelperDeserializer implements JsonDeserializer<FbPhotosHelper> {
    @Override
    public FbPhotosHelper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray idArray = json.getAsJsonObject().getAsJsonArray("data");
        List<String> ids = new ArrayList<>(idArray.size());
        for (JsonElement element : idArray) {
            ids.add(element.getAsJsonObject().get("id").getAsString());
        }
        FbPhotosHelper fbPhotosHelper = new FbPhotosHelper();
        fbPhotosHelper.setIds(ids);
        return fbPhotosHelper;
    }
}
