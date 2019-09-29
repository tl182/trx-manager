package com.trxmanager.manager.util.retry;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Builder
public class Retry {

    private int retryCount;
    private int delayMillis;

    @Singular
    private Set<Class<? extends Exception>> retryExceptions;

    @Singular
    private Set<Class<? extends Exception>> passExceptions;

    public void execute(Runnable operation) throws MaxNumberOfRetriesException, RetryRuntimeException {
        Exception lastException = null;

        for (int i = 0; i < retryCount; i++) {
            try {
                operation.run();
                return;
            } catch (Exception e) {
                if (retryExceptions.contains(e.getClass())) {
                    lastException = e;
                } else if (passExceptions.contains(e.getClass())) {
                    throw e;
                } else {
                    throw new RetryRuntimeException("Unexpected exception", e);
                }
            }

            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                throw new RetryRuntimeException("Interrupted", e);
            }
        }

        throw new MaxNumberOfRetriesException(lastException);
    }
}
