package ua.dp.strahovik.yalantistask1.entities.deserializers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.entities.Address;
import ua.dp.strahovik.yalantistask1.entities.Company;
import ua.dp.strahovik.yalantistask1.entities.Event;
import ua.dp.strahovik.yalantistask1.entities.EventState;

public class EventDeserializer implements JsonDeserializer<Event> {
    private Context mContext;

    public EventDeserializer(Context context) {
        mContext = context;
    }

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject eventObj = json.getAsJsonObject();
        Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Address.class, new AddressDeserializer())
                .registerTypeAdapter(EventState.class, new EventStateDeserializer())
                .create();
        Event event = g.fromJson(json, Event.class);
        parseResponsibleCompany(eventObj, event);
        parseUriList(eventObj, event);
        return event;
    }

    private void parseResponsibleCompany(JsonObject eventObj, Event event) {
        JsonArray companyArray = eventObj.getAsJsonArray("performers");
        if (companyArray.size() > 0) {
            String companyName = companyArray
                    .get(0)
                    .getAsJsonObject()
                    .get("organization")
                    .getAsString();
            Company company = new Company();
            company.setName(companyName);
            event.setResponsible(company);
        }
    }

    private void parseUriList(JsonObject eventObj, Event event) {
        JsonArray photosArray = eventObj.getAsJsonArray("files");
        List<URI> photos = new ArrayList<>(photosArray.size());
        for (JsonElement element : photosArray) {
            String filename = element.getAsJsonObject()
                    .get("filename")
                    .getAsString();
            try {
                photos.add(new URI(mContext.getString(
                        R.string.single_event_activity_event_deserializer_event_photos_initial_uri_path) + filename));
            } catch (URISyntaxException e) {
                Log.e(mContext.getString(R.string.log_tag), mContext.getString(
                        R.string.error_event_deserializer_parsing_photo_uri_) + e.toString());
            }
        }
        event.setPhotos(photos);
    }

}
