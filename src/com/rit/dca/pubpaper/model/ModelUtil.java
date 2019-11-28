package com.rit.dca.pubpaper.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ModelUtil {

    public static String getJson(Object modelObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(modelObject);
        return json;
    }

}
