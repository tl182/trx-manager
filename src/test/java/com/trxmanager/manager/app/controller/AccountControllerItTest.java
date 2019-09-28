package com.trxmanager.manager.app.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static com.trxmanager.manager.util.Const.HttpStatus.OK;
import static com.trxmanager.manager.util.Const.RequestMethod.GET;
import static com.trxmanager.manager.util.Const.RequestMethod.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountControllerItTest extends AbstractControllerItTest {

    @BeforeEach
    public void setUp() {
        startTestServer();
    }

    @AfterEach
    public void tearDown() {
        stopTestServer();
    }

    @Test
    public void testGetAccount() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/accounts/1")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(OK, response.getStatus());

        Map<String, Object> expectedBody = new HashMap<>();
        expectedBody.put("id", 1.0);
        expectedBody.put("balance", Double.valueOf("13.336"));

        assertEquals(expectedBody, fromJson(response.getBody(), Map.class));
    }

    @Test
    public void testGetAccount_nonExistent() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/accounts/3")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testGetAccount_badRequest() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/accounts/blah")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testCreateAccount() {
        Double balance = Double.valueOf("765.4321");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("balance", balance);

        TestRequest request = TestRequest.builder()
                .method(POST)
                .path("/accounts")
                .body(toJson(requestBody))
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(OK, response.getStatus());

        Map responseBody = fromJson(response.getBody(), Map.class);
        assertNotNull(responseBody.get("id"));
        assertEquals(balance, responseBody.get("balance"));
    }

    @Test
    public void testCreateAccount_noBalanceSpecified() {
        TestRequest request = TestRequest.builder()
                .method(POST)
                .path("/accounts")
                .body(toJson(new HashMap<>()))
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testCreateAccount_badRequest() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("balance", "blah");

        TestRequest request = TestRequest.builder()
                .method(POST)
                .path("/accounts")
                .body(toJson(requestBody))
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }
}
