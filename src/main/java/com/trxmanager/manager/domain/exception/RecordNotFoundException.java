package com.trxmanager.manager.domain.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
