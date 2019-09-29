package com.trxmanager.manager.util;

import com.google.gson.Gson;

import java.util.Optional;

public abstract class Conversions {

    public static Optional<Long> parseLong(String id) {
        try {
            return Optional.of(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> fromJson(String json, Class<T> tClass) {
        try {
            return Optional.ofNullable(new Gson().fromJson(json, tClass));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }
}
