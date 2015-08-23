package br.com.jeanpimentel.usergenerator.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import br.com.jeanpimentel.usergenerator.model.User;
import br.com.jeanpimentel.usergenerator.service.RandomUserDeserializer;


public class UserRepository {

    private static final String URL = "https://randomuser.me/api";

    static public void getUser(Context context, final FutureCallback<User> userCallback) {

        Ion.with(context).load(URL).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(User.class, new RandomUserDeserializer());
                Gson gson = gsonBuilder.create();

                User user = gson.fromJson(result, User.class);
                userCallback.onCompleted(e, user);
            }
        });
    }

}
