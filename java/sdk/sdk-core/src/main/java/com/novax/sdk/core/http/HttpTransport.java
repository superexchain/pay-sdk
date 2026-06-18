package com.novax.sdk.core.http;

import java.io.IOException;

/**
 * Pluggable transport. The default {@link JdkHttpTransport} uses
 * {@code java.net.http.HttpClient}; swap in OkHttp / Reactor Netty / etc. by
 * implementing this interface.
 */
public interface HttpTransport {

    SdkResponse execute(SdkRequest request) throws IOException;
}
