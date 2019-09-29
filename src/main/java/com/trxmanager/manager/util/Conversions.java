package com.trxmanager.manager.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.trxmanager.manager.app.exception.JsonDeserializationException;

public abstract class Conversions {

    public static <T> T fromJson(String json, Class<T> tClass) {
        try {
            return new Gson().fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            throw new JsonDeserializationException("Could not deserialize json value", e);
        }
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }
}
