package com.trxmanager.manager.app.controller;

import com.trxmanager.manager.app.App;
import lombok.Builder;
import lombok.Value;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.OK;

public class AbstractControllerItTest {

    private static final int PORT = 4567;
    private static final String CONNECTION_STRING = "jdbc:h2:mem:trx_manager";
    private static final String INIT_SCRIPT_LOCATION = "classpath:db/migration/init-test.sql";
    private static final boolean ENABLE_H2_CONSOLE = true;

    private static final Map<String, String> DEFAULT_HEADERS = Collections.unmodifiableMap(
            new HashMap<String, String>() {{
                put("Content-Type", APPLICATION_JSON);
                put("charset", "utf-8");
            }}
    );

    private App app;

    protected void startTestServer() {
        app = new App(PORT, CONNECTION_STRING, INIT_SCRIPT_LOCATION, ENABLE_H2_CONSOLE);
        app.init();
    }

    protected void stopTestServer() {
        app.shutDown();
    }

    protected static TestResponse sendRequest(TestRequest request) {
        try {
            URL url = new URL("http://localhost:" + PORT + request.getPath());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());
            request.getHeaders().forEach(connection::setRequestProperty);

            if (request.getBody() != null) {
                byte[] body = request.getBody().getBytes(StandardCharsets.UTF_8);
                DEFAULT_HEADERS.forEach(connection::setRequestProperty);
                connection.setRequestProperty("Content-Length", Integer.toString(body.length));
                connection.setDoOutput(true);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(body);
                }
            }

            request.getHeaders().forEach(connection::setRequestProperty);

            connection.connect();

            int responseCode = connection.getResponseCode();
            String body = null;
            if (responseCode == OK) {
                body = IOUtils.toString(connection.getInputStream());
            }

            return TestResponse.builder()
                    .body(body)
                    .status(responseCode)
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
