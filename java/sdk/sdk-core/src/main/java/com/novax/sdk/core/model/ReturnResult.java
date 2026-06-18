package com.novax.sdk.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mirror of the server-side response envelope. {@code code == 0} is treated as
 * success; adjust {@link #isSuccess()} if the backend uses a different convention.
 */
public record ReturnResult<T>(
        Integer code,
        String message,
        T data
) {

    @JsonCreator
    public ReturnResult(
            @JsonProperty("code") Integer code,
            @JsonProperty("message") String message,
            @JsonProperty("data") T data
    ) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
