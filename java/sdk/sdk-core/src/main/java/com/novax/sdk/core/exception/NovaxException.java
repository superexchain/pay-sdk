package com.novax.sdk.core.exception;

/** Base type for everything thrown by the SDK. */
public class NovaxException extends RuntimeException {

    public NovaxException(String message) {
        super(message);
    }

    public NovaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
