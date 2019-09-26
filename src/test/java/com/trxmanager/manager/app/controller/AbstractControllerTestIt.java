package com.trxmanager.manager.app.controller;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Value;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.trxmanager.manager.Main.runServer;
import static com.trxmanager.manager.app.DbModule.CONNECTION_STRING;
import static spark.Spark.*;

public class AbstractControllerTestIt {

    private static final int PORT = 4567;
    private static final String INITIALIZING_CONNECTION_STRING =
            CONNECTION_STRING + ";INIT=RUNSCRIPT FROM 'classpath:db/migration/init-test.sql'";

    private static final Gson GSON = new Gson();

    protected static void startTestServer() {
        runServer(PORT, CONNECTION_STRING, false);
        awaitInitialization();
    }

    protected static void stopTestServer() {
        stop();
        awaitStop();
    }

    protected static String toJson(Object object) {
        return GSON.toJson(object);
    }

    protected static <T> T fromJson(String json, Class<T> tClass) {
        return GSON.fromJson(json, tClass);
    }

    protected static TestResponse sendRequest(TestRequest request) {
        try {
            URL url = new URL("http://localhost:" + PORT + request.getPath());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());
            request.getHeaders().forEach(connection::setRequestProperty);
            connection.connect();

            String body = IOUtils.toString(connection.getInputStream());

            return TestResponse.builder()
                    .body(body)
                    .status(connection.getResponseCode())
                    .headers(connection.getHeaderFields())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value
    @Builder
    protected static class TestRequest {
        private String method;
        private String path;
        private String body;

        @Builder.Default
        private Map<String, String> headers = Collections.emptyMap();
    }

    @Value
    @Builder
    protected static class TestResponse {
        private String body;
        private int status;

        @Builder.Default
        private Map<String, List<String>> headers = Collections.emptyMap();
    }
}
