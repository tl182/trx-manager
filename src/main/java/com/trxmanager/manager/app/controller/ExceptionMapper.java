package com.trxmanager.manager.app.controller;

import com.trxmanager.manager.app.exception.IdFormatException;
import com.trxmanager.manager.app.exception.InvalidValueException;
import com.trxmanager.manager.app.exception.JsonDeserializationException;
import com.trxmanager.manager.domain.exception.NotEnoughFundsException;
import com.trxmanager.manager.domain.exception.RecordNotFoundException;
import com.trxmanager.manager.domain.exception.TransferCreationException;
import com.trxmanager.manager.domain.exception.TransferExecutionException;
import com.trxmanager.manager.util.Conversions;
import com.trxmanager.manager.util.retry.RetryRuntimeException;
import lombok.extern.slf4j.Slf4j;
import spark.ExceptionHandler;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static com.trxmanager.manager.util.Const.HttpStatus.INTERNAL_SERVER_ERROR;
import static spark.Spark.exception;

@Slf4j
public class ExceptionMapper {

    public void handleExceptions() {
        exception(IdFormatException.class, badRequest());
        exception(InvalidValueException.class, badRequest());
        exception(JsonDeserializationException.class, badRequest());
        exception(RecordNotFoundException.class, badRequest());
        exception(NotEnoughFundsException.class, badRequest());
        exception(TransferCreationException.class, badRequest());

        exception(RetryRuntimeException.class, internalError());
        exception(TransferExecutionException.class, internalError());
    }

    private <T extends Exception> ExceptionHandler<T> badRequest() {
        return (exc, req, res) -> {
            log.info("Caught expected exception: {}", exc.getMessage());
            res.type(APPLICATION_JSON);
            res.status(BAD_REQUEST);
            res.body(Conversions.toJson(exc.getMessage()));
        };
    }

    private <T extends Exception> ExceptionHandler<T> internalError() {
        return (exc, req, res) -> {
            log.error("Caught unexpected exception", exc);
            res.type(APPLICATION_JSON);
            res.status(INTERNAL_SERVER_ERROR);
            res.body(Conversions.toJson("Internal server error"));
        };
    }
}
