package com.trxmanager.manager.util;

public abstract class Const {

    public static abstract class ContentType {
        public static final String APPLICATION_JSON = "application/json";
    }

    public static abstract class RequestMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
    }

    public static abstract class HttpStatus {
        public static final Integer OK = 200;
        public static final Integer BAD_REQUEST = 400;
        public static final Integer INTERNAL_SERVER_ERROR = 500;
    }
}
