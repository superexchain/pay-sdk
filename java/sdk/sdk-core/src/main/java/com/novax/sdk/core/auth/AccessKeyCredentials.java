package com.novax.sdk.core.auth;

import java.util.Objects;

/**
 * Access-key + access-secret pair. The server resolves {@code accessKey} to an
 * {@code ApiKey} record (containing {@code userId}, IP whitelist, expiry, …)
 * and re-derives the signature with the matching secret — so the SDK never
 * needs to know the {@code userId}, it is injected server-side.
 */
public final class AccessKeyCredentials implements Credentials {

    private final String accessKey;
    private final String accessSecret;

    public AccessKeyCredentials(String accessKey, String accessSecret) {
        this.accessKey = Objects.requireNonNull(accessKey, "accessKey");
        this.accessSecret = Objects.requireNonNull(accessSecret, "accessSecret");
    }

    public String accessKey() {
        return accessKey;
    }

    public String accessSecret() {
        return accessSecret;
    }
}
