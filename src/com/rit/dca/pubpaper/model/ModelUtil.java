package com.rit.dca.pubpaper.model;

import com.google.gson.Gson;

public class ModelUtil {

    public static String getJson(Object modelObject) {
        Gson gson = new Gson();
        String json = gson.toJson(modelObject);
        return json;
    }

}
