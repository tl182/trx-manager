package com.trxmanager.manager.util.retry;

public class MaxNumberOfRetriesException extends RuntimeException {

    public MaxNumberOfRetriesException(Throwable cause) {
        super(cause);
    }
}
