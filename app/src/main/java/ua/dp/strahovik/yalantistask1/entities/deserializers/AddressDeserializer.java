package ua.dp.strahovik.yalantistask1.entities.deserializers;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ua.dp.strahovik.yalantistask1.entities.Address;

public class AddressDeserializer implements JsonDeserializer<Address> {

    @Override
    public Address deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject addressObj = json.getAsJsonObject();
        Address address = new Address();
        address.setDistrict(addressObj.get("district").getAsJsonObject().get("name").getAsString());
        address.setCity(addressObj.get("city").getAsJsonObject().get("name").getAsString());
        JsonObject streetObj = addressObj.get("street").getAsJsonObject();
        address.setStreet(streetObj.get("name").getAsString());
        JsonObject streetType = streetObj.get("street_type").getAsJsonObject();
        address.setStreetTypeName(streetType.get("name").getAsString());
        address.setStreetTypeShortName(streetType.get("short_name").getAsString());
        address.setHouse(addressObj.get("house").getAsJsonObject().get("name").getAsString());
        address.setFlat(addressObj.get("flat").getAsString());
        return address;
    }
}
