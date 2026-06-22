package com.novax.sdk.core.http.interceptors;

import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Stamps a fixed set of headers onto every request — used for static caller
 * context like {@code ip} (for the server-side whitelist check) and
 * {@code language}.
 *
 * <p>Headers are <em>appended</em>: if the request already supplies the same
 * name, both values are kept on the wire (HTTP allows the same header name to
 * appear multiple times; per RFC 7230 §3.2.2 receivers join them with
 * {@code ,}). If you instead want the per-request value to replace the default,
 * use {@code SdkRequest#setHeaders} from a custom interceptor.
 */
public final class HeaderInterceptor implements Interceptor {

    private final Map<String, String> defaults;

    public HeaderInterceptor(Map<String, String> defaults) {
        this.defaults = Map.copyOf(defaults);
    }

    @Override
    public SdkResponse intercept(Chain chain) throws IOException {
        SdkRequest req = chain.request();
        return chain.proceed(defaults.isEmpty() ? req : req.appendHeaders(defaults));
    }
}
