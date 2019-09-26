package com.trxmanager.manager.util;

public abstract class Const {

    public static abstract class ContentType {
        public static final String APPLICATION_JSON = "application/json";
    }

    public static abstract class RequestMethod {
        public static final String GET = "GET";
    }

    public static abstract class HttpStatus {
        public static final Integer OK = 200;
        public static final Integer BAD_REQUEST = 400;
    }
}
