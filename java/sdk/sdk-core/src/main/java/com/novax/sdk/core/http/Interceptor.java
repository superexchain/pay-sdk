package com.novax.sdk.core.http;

import java.io.IOException;

/**
 * Cross-cutting hook for outbound requests / inbound responses. All such
 * concerns — auth, retry, logging, header injection — live as interceptors,
 * not as ad-hoc logic in the client.
 *
 * <p>Implementations <em>must</em> call {@code chain.proceed(...)} exactly once
 * (or short-circuit by building a {@link SdkResponse} themselves).
 */
public interface Interceptor {

    SdkResponse intercept(Chain chain) throws IOException;

    interface Chain {
        SdkRequest request();

        SdkResponse proceed(SdkRequest request) throws IOException;
    }
}
