package com.trxmanager.manager.util.retry;

public class RetryRuntimeException extends RuntimeException {

    public RetryRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
