package com.trxmanager.manager.app.controller;

import com.trxmanager.manager.util.Conversions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

class TransferControllerItTest extends AbstractControllerItTest {

    @BeforeEach
    public void setUp() {
        startTestServer();
    }

    @AfterEach
    public void tearDown() {
        stopTestServer();
    }

    @Test
    public void testGetTransfer() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/transfers/1")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(OK, response.getStatus());

        Map<String, Object> expectedBody = new HashMap<>();
        expectedBody.put("id", 1.0);
        expectedBody.put("fromId", 1.0);
        expectedBody.put("toId", 2.0);
        expectedBody.put("amount", Double.valueOf("10"));
        expectedBody.put("status", "FAILED");

        Map actual = Conversions.fromJson(response.getBody(), Map.class).orElseGet(Assertions::fail);
        assertEquals(expectedBody, actual);
    }

    @Test
    public void testGetTransfer_nonExistent() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/transfers/3")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testGetTransfer_badRequest() {
        TestRequest request = TestRequest.builder()
                .method(GET)
                .path("/transfers/blah")
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    public void testCreateTransfer() {
        Double fromId = 1.0;
        Double toId = 2.0;
        Double amount = Double.valueOf("10.0");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromId", fromId);
        requestBody.put("toId", toId);
        requestBody.put("amount", amount);

        TestRequest request = TestRequest.builder()
                .method(POST)
                .path("/transfers")
                .body(Conversions.toJson(requestBody))
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(OK, response.getStatus());

        Map responseBody = Conversions.fromJson(response.getBody(), Map.class).orElseGet(Assertions::fail);
        assertNotNull(responseBody.get("id"));
        assertEquals(fromId, responseBody.get("fromId"));
        assertEquals(toId, responseBody.get("toId"));
        assertEquals(amount, responseBody.get("amount"));
        assertEquals("CREATED", responseBody.get("status"));
    }

    @Test
    public void testCreateTransfer_badRequest() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fromId", "blah");
        requestBody.put("toId", "blah");
        requestBody.put("amount", "blah");

        TestRequest request = TestRequest.builder()
                .method(POST)
                .path("/transfers")
                .body(Conversions.toJson(requestBody))
                .build();

        TestResponse response = sendRequest(request);
        assertEquals(BAD_REQUEST, response.getStatus());
    }
}