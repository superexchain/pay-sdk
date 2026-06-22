package com.novax.sdk.core;

import com.novax.sdk.core.auth.Credentials;
import com.novax.sdk.core.http.HttpTransport;
import com.novax.sdk.core.http.Interceptor;
import com.novax.sdk.core.http.JdkHttpTransport;
import com.novax.sdk.core.json.JsonMapper;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.time.Duration;
import java.util.*;

/**
 * Immutable client configuration built via {@link #builder()}.
 */
public final class NovaxConfig {

    private final URI endpoint;
    private final Credentials credentials;
    private final Duration timeout;
    private final Map<String, String> defaultHeaders;
    private final List<Interceptor> interceptors;
    private final HttpTransport transport;
    private final JsonMapper jsonMapper;

    private NovaxConfig(Builder b) {
        this.endpoint = Objects.requireNonNull(b.endpoint, "endpoint");
        this.credentials = b.credentials;
        this.timeout = b.timeout;
        this.defaultHeaders = Map.copyOf(b.defaultHeaders);
        this.interceptors = List.copyOf(b.interceptors);
        if (b.transport != null) {
            this.transport = b.transport;
        } else {
            SSLContext ctx = b.insecureTls ? JdkHttpTransport.insecureTrustAllSslContext() : null;
            this.transport = new JdkHttpTransport(b.timeout, ctx);
        }
        this.jsonMapper = b.jsonMapper != null ? b.jsonMapper : JsonMapper.defaultMapper();
    }

    public URI endpoint() {
        return endpoint;
    }

    public Credentials credentials() {
        return credentials;
    }

    public Duration timeout() {
        return timeout;
    }

    public Map<String, String> defaultHeaders() {
        return defaultHeaders;
    }

    public List<Interceptor> interceptors() {
        return interceptors;
    }

    public HttpTransport transport() {
        return transport;
    }

    public JsonMapper jsonMapper() {
        return jsonMapper;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private URI endpoint;
        private Credentials credentials;
        private Duration timeout = Duration.ofSeconds(30);
        private final Map<String, String> defaultHeaders = new LinkedHashMap<>();
        private final List<Interceptor> interceptors = new ArrayList<>();
        private HttpTransport transport;
        private JsonMapper jsonMapper;
        private boolean insecureTls;

        public Builder endpoint(URI uri) {
            this.endpoint = uri;
            return this;
        }

        public Builder credentials(Credentials c) {
            this.credentials = c;
            return this;
        }

        public Builder timeout(Duration d) {
            this.timeout = d;
            return this;
        }

        public Builder defaultHeader(String name, String value) {
            this.defaultHeaders.put(name, value);
            return this;
        }

        public Builder addInterceptor(Interceptor i) {
            this.interceptors.add(i);
            return this;
        }

        public Builder transport(HttpTransport t) {
            this.transport = t;
            return this;
        }

        public Builder jsonMapper(JsonMapper m) {
            this.jsonMapper = m;
            return this;
        }

        /**
         * <b>DEV ONLY.</b> Disables TLS certificate validation. Use to hit an
         * internal / self-signed dev host.
         */
        public Builder insecureTls() {
            this.insecureTls = true;
            return this;
        }

        public NovaxConfig build() {
            return new NovaxConfig(this);
        }
    }
}
