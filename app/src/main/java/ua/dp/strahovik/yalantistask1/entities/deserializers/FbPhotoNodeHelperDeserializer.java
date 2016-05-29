package ua.dp.strahovik.yalantistask1.entities.deserializers;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.dp.strahovik.yalantistask1.entities.helpers.FbPhotoNodeHelper;

public class FbPhotoNodeHelperDeserializer implements JsonDeserializer<FbPhotoNodeHelper> {
    @Override
    public FbPhotoNodeHelper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonArray photoArr = json.getAsJsonObject().get("images").getAsJsonArray();
        List<FbPhotoNodeHelper.PlatformImageSource> sources = new ArrayList<>(photoArr.size());
        JsonObject tmpObj;
        for (JsonElement element : photoArr) {
            tmpObj = element.getAsJsonObject();
            sources.add(FbPhotoNodeHelper.platformImageSourceFactory(
                    tmpObj.get("height").getAsInt(),
                    tmpObj.get("source").getAsString(),
                    tmpObj.get("width").getAsInt()
            ));
        }
        FbPhotoNodeHelper fbPhotoNodeHelper = new FbPhotoNodeHelper();
        fbPhotoNodeHelper.setPlatformImageSources(sources);
        return fbPhotoNodeHelper;
    }
}
