package com.novax.sdk.pay.h5;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Wraps the raw token string. Server returns {@code ReturnResult<String>}
 * where {@code data} is the token itself (not an object), so we use a
 * delegating {@link JsonCreator} to unwrap it.
 */
public record PayTokenResponse(@JsonValue String token) {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PayTokenResponse of(String value) {
        return new PayTokenResponse(value);
    }
}
