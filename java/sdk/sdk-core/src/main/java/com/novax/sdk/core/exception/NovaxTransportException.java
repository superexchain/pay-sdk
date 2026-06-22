package com.novax.sdk.core.exception;

/**
 * Thrown for I/O errors, timeouts, or anything that prevents getting a response.
 */
public class NovaxTransportException extends NovaxException {

    public NovaxTransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
