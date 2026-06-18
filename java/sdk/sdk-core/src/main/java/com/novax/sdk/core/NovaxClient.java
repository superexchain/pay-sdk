package com.novax.sdk.core;

import com.novax.sdk.core.auth.AccessKeyCredentials;
import com.novax.sdk.core.exception.NovaxApiException;
import com.novax.sdk.core.exception.NovaxTransportException;
import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.InterceptorChain;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;
import com.novax.sdk.core.http.interceptors.HeaderInterceptor;
import com.novax.sdk.core.http.interceptors.SignatureInterceptor;
import com.novax.sdk.core.model.ReturnResult;
import com.novax.sdk.core.request.ApiRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Sole entry point. Adding a new endpoint never requires touching this class —
 * just pass an {@link ApiRequest} of any shape to {@link #execute}.
 */
public final class NovaxClient {

    private final NovaxConfig config;
    private final InterceptorChain chain;

    private NovaxClient(NovaxConfig cfg) {
        this.config = cfg;
        List<Interceptor> all = new ArrayList<>(cfg.interceptors());
        if (!cfg.defaultHeaders().isEmpty()) {
            all.add(new HeaderInterceptor(cfg.defaultHeaders()));
        }
        if (cfg.credentials() instanceof AccessKeyCredentials ak) {
            all.add(new SignatureInterceptor(ak, cfg.jsonMapper()));
        }
        this.chain = new InterceptorChain(cfg.transport(), all);
    }

    public static Builder builder() {
        return new Builder();
    }

    public <R> R execute(ApiRequest<R> request) {
        SdkRequest sdkReq = SdkRequest.from(config.endpoint(), request, config.jsonMapper());
        SdkResponse resp;
        try {
            resp = chain.proceed(sdkReq);
        } catch (IOException e) {
            throw new NovaxTransportException("HTTP request failed: " + sdkReq.uri(), e);
        }
        ReturnResult<R> wrapped = config.jsonMapper()
                .readReturnResult(resp.body(), request.responseType());
        if (!wrapped.isSuccess()) {
            throw new NovaxApiException(wrapped.code(), wrapped.message());
        }
        return wrapped.data();
    }

    public static final class Builder {

        private final NovaxConfig.Builder cfg = NovaxConfig.builder();

        public Builder endpoint(String e) {
            cfg.endpoint(URI.create(e));
            return this;
        }

        public Builder accessKey(String accessKey, String accessSecret) {
            cfg.credentials(new AccessKeyCredentials(accessKey, accessSecret));
            return this;
        }

        /** Sent as the {@code ip} header — used for the server-side IP whitelist check. */
        public Builder clientIp(String ip) {
            cfg.defaultHeader("ip", ip);
            return this;
        }

        public Builder language(String lang) {
            cfg.defaultHeader("language", lang);
            return this;
        }

        public Builder addInterceptor(Interceptor i) {
            cfg.addInterceptor(i);
            return this;
        }

        /** <b>DEV ONLY.</b> Trust any TLS cert — for hitting internal /
         *  self-signed hosts. Never enable in production. */
        public Builder insecureTls() {
            cfg.insecureTls();
            return this;
        }

        public Builder config(Consumer<NovaxConfig.Builder> fn) {
            fn.accept(cfg);
            return this;
        }

        public NovaxClient build() {
            return new NovaxClient(cfg.build());
        }
    }
}
