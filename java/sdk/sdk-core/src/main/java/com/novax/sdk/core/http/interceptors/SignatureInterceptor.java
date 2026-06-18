package com.novax.sdk.core.http.interceptors;

import com.novax.sdk.core.auth.AccessKeyCredentials;
import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;
import com.novax.sdk.core.http.SignatureCodec;
import com.novax.sdk.core.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.LongSupplier;

/**
 * Stamps {@code X-Access-Key} / {@code X-Signature} / {@code X-Timestamp} onto
 * every outbound request that needs server-side signature verification.
 *
 * <p>Public paths (e.g. {@code /pay/public/*}) bypass signing — they
 * authenticate via a {@code token} query parameter instead.
 */
public final class SignatureInterceptor implements Interceptor {

    private final AccessKeyCredentials credentials;
    private final JsonMapper jsonMapper;
    private final LongSupplier clock;

    public SignatureInterceptor(AccessKeyCredentials credentials, JsonMapper jsonMapper) {
        this(credentials, jsonMapper, System::currentTimeMillis);
    }

    public SignatureInterceptor(AccessKeyCredentials credentials, JsonMapper jsonMapper,
                                LongSupplier clock) {
        this.credentials = credentials;
        this.jsonMapper = jsonMapper;
        this.clock = clock;
    }

    @Override
    public SdkResponse intercept(Chain chain) throws IOException {
        SdkRequest req = chain.request();
        if (!shouldSign(req.uri())) {
            return chain.proceed(req);
        }
        long ts = clock.getAsLong();
        String dataToSign = SignatureCodec.dataToSign(
                req.method(), req.uri(), req.body(), ts, jsonMapper);
        String signature = SignatureCodec.hmacSha256Hex(dataToSign, credentials.accessSecret());

        Map<String, String> signed = new LinkedHashMap<>();
        signed.put("X-Access-Key", credentials.accessKey());
        signed.put("X-Signature", signature);
        signed.put("X-Timestamp", Long.toString(ts));
        return chain.proceed(req.withHeaders(signed));
    }

    /** Mirrors the server-side filter URL pattern — only {@code /pay/v3/*}
     *  and {@code /free-spot/v3/*} are signed; public paths pass through. */
    private static boolean shouldSign(URI uri) {
        String path = uri.getPath();
        return path != null && (path.startsWith("/api/pay/v3/"));
    }
}
