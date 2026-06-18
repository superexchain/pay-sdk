package com.novax.sdk.core.http.interceptors;

import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Minimal stdout/stderr-style request logger. Replace with SLF4J/Logback at will. */
public final class LoggingInterceptor implements Interceptor {

    private static final Logger LOG = Logger.getLogger(LoggingInterceptor.class.getName());

    @Override
    public SdkResponse intercept(Chain chain) throws IOException {
        SdkRequest req = chain.request();
        long t0 = System.nanoTime();
        LOG.log(Level.INFO, () -> "-> " + req.method() + " " + req.uri());
        SdkResponse resp = chain.proceed(req);
        long ms = (System.nanoTime() - t0) / 1_000_000;
        LOG.log(Level.INFO, () -> "<- " + resp.statusCode() + " (" + ms + "ms)");
        return resp;
    }
}
