package com.novax.sdk.core.http.interceptors;

import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stamps a fixed set of headers onto every request — used for static caller
 * context like {@code ip} (for the server-side whitelist check) and
 * {@code language}. Per-request headers already on the request take precedence.
 */
public final class HeaderInterceptor implements Interceptor {

    private final Map<String, String> defaults;

    public HeaderInterceptor(Map<String, String> defaults) {
        this.defaults = Map.copyOf(defaults);
    }

    @Override
    public SdkResponse intercept(Chain chain) throws IOException {
        SdkRequest req = chain.request();
        Map<String, String> existing = req.headers();
        Map<String, String> toAdd = new LinkedHashMap<>();
        for (var e : defaults.entrySet()) {
            if (!existing.containsKey(e.getKey())) {
                toAdd.put(e.getKey(), e.getValue());
            }
        }
        return chain.proceed(toAdd.isEmpty() ? req : req.withHeaders(toAdd));
    }
}
