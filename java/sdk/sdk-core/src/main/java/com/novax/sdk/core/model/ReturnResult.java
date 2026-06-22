package com.novax.sdk.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mirror of the server-side response envelope. {@code code == 0} is treated as
 * success; adjust {@link #isSuccess()} if the backend uses a different convention.
 */
public record ReturnResult<T>(
        Integer code,
        String msg,
        T data
) {

    @JsonCreator
    public ReturnResult(
            @JsonProperty("code") Integer code,
            @JsonProperty("msg") String msg,
            @JsonProperty("data") T data
    ) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
