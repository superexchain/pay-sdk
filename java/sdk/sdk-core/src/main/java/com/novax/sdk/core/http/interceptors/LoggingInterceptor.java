package com.novax.sdk.core.http.interceptors;

import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Minimal stdout/stderr-style request logger. Replace with SLF4J/Logback at will.
 *
 * <p>Normal responses log at {@code INFO} (status + elapsed). Server-error
 * responses ({@code statusCode >= 500}) log at {@code SEVERE} and include the
 * response body (truncated to {@link #ERROR_BODY_MAX_CHARS} chars) so a 500 in
 * production leaves enough breadcrumbs to diagnose.
 */
public final class LoggingInterceptor implements Interceptor {

    private static final Logger LOG = Logger.getLogger(LoggingInterceptor.class.getName());
    private static final int ERROR_BODY_MAX_CHARS = 2048;

    @Override
    public SdkResponse intercept(Chain chain) throws IOException {
        SdkRequest req = chain.request();
        long t0 = System.nanoTime();
        LOG.log(Level.INFO, () -> "-> " + req.method() + " " + req.uri());
        SdkResponse resp = chain.proceed(req);
        long ms = (System.nanoTime() - t0) / 1_000_000;

        if (resp.statusCode() >= 500) {
            LOG.log(Level.SEVERE, () -> "<- " + resp.statusCode() + " (" + ms + "ms) "
                    + req.method() + " " + req.uri()
                    + " body=" + truncate(bodyAsString(resp), ERROR_BODY_MAX_CHARS));
        } else {
            LOG.log(Level.INFO, () -> "<- " + resp.statusCode() + " (" + ms + "ms)");
        }
        return resp;
    }

    private static String bodyAsString(SdkResponse resp) {
        byte[] body = resp.body();
        if (body == null || body.length == 0) return "<empty>";
        return new String(body, StandardCharsets.UTF_8);
    }

    private static String truncate(String s, int max) {
        if (s.length() <= max) return s;
        return s.substring(0, max) + "…(+" + (s.length() - max) + " chars)";
    }
}
