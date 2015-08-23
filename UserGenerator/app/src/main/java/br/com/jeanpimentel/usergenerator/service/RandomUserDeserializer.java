package br.com.jeanpimentel.usergenerator.service;

import android.text.format.Formatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

import br.com.jeanpimentel.usergenerator.model.User;

public class RandomUserDeserializer implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (json.isJsonObject()) {
            final JsonObject jsonObject = json.getAsJsonObject();

            JsonArray users = jsonObject.getAsJsonArray("results");
            if (users.size() == 0) {
                return null;
            }

            JsonObject jsonUser = (JsonObject)users.get(0).getAsJsonObject().get("user");
            User user = new User();

            JsonObject name = (JsonObject)jsonUser.get("name");
            JsonObject picture = (JsonObject)jsonUser.get("picture");

            user.name = String.format("%s %s", name.get("first").getAsString(), name.get("last").getAsString());
            user.email = jsonUser.get("email").getAsString();
            user.username = jsonUser.get("username").getAsString();
            user.password = jsonUser.get("password").getAsString();
            user.photo = picture.get("medium").getAsString();
            return user;
        }

        return null;
    }
}
