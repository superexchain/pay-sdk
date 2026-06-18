package com.novax.sdk.core.http;

import java.util.List;
import java.util.Map;

/** Raw response handed back by the transport layer to the interceptor chain. */
public record SdkResponse(
        int statusCode,
        Map<String, List<String>> headers,
        byte[] body
) { }
