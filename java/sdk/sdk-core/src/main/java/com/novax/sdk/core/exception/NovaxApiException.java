package com.novax.sdk.core.exception;

/**
 * Thrown when the server returns a non-success business code in {@code ReturnResult}.
 */
public class NovaxApiException extends NovaxException {

    private final Integer code;

    public NovaxApiException(Integer code, String message) {
        super("[" + code + "] " + message);
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
