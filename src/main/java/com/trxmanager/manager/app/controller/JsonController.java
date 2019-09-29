package com.trxmanager.manager.app.controller;

import com.trxmanager.manager.app.exception.IdFormatException;
import com.trxmanager.manager.util.Conversions;
import spark.Route;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static spark.Spark.*;

public abstract class JsonController implements Controller {

    protected abstract String getContextRoute();

    protected void getJson(String path, Route route) {
        get(getContextRoute() + path, wrapContentType(route), Conversions::toJson);
    }

    protected void postJson(Route route) {
        postJson("", route);
    }

    protected void postJson(String path, Route route) {
        post(getContextRoute() + path, APPLICATION_JSON, wrapContentType(route), Conversions::toJson);
    }

    protected void putJson(String path, Route route) {
        put(getContextRoute() + path, APPLICATION_JSON, wrapContentType(route), Conversions::toJson);
    }

    protected static Long parseId(String id) {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IdFormatException("Wrong id format", e);
        }
    }

    private Route wrapContentType(Route route) {
        return (req, res) -> {
            res.type(APPLICATION_JSON);
            return route.handle(req, res);
        };
    }
}
