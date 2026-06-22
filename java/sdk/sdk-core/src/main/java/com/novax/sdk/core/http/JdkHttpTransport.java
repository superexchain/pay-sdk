package com.novax.sdk.core.http;

import com.novax.sdk.core.exception.NovaxException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

public final class JdkHttpTransport implements HttpTransport {

    private final HttpClient client;
    private final Duration requestTimeout;

    public JdkHttpTransport(Duration requestTimeout) {
        this(requestTimeout, null);
    }

    public JdkHttpTransport(Duration requestTimeout, SSLContext sslContext) {
        this.requestTimeout = requestTimeout;
        HttpClient.Builder b = HttpClient.newBuilder().connectTimeout(requestTimeout);
        if (sslContext != null) {
            b.sslContext(sslContext);
        }
        this.client = b.build();
    }

    @Override
    public SdkResponse execute(SdkRequest req) throws IOException {
        HttpRequest.Builder b = HttpRequest.newBuilder(req.uri()).timeout(requestTimeout);
        // emit one .header() call per value — repeated invocations add to the
        // same header name without overwriting (multi-value HTTP)
        req.headers().forEach((name, values) -> {
            for (String v : values) b.header(name, v);
        });
        HttpRequest.BodyPublisher publisher = req.body() == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofByteArray(req.body());
        b.method(req.method().name(), publisher);
        try {
            HttpResponse<byte[]> resp = client.send(b.build(), HttpResponse.BodyHandlers.ofByteArray());
            return new SdkResponse(resp.statusCode(), resp.headers().map(), resp.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("interrupted while waiting for response", e);
        }
    }

    /**
     * Builds an {@link SSLContext} that trusts <em>all</em> certificates and
     * skips hostname verification.
     *
     * <p><b>DEV ONLY.</b> Disables protection against man-in-the-middle attacks.
     * Use this for hitting an internal/self-signed dev host; <em>never</em>
     * enable in production.
     */
    public static SSLContext insecureTrustAllSslContext() {
        TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }
                }
        };
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, trustAll, new SecureRandom());
            return ctx;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new NovaxException("failed to build insecure SSLContext", e);
        }
    }
}
