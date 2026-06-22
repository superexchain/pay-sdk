package com.novax.sdk.core.request;

import java.util.Map;

/**
 * Self-describing API request. A request knows its own HTTP method, path,
 * headers, body, and the type of response it expects.
 *
 * <p>The whole SDK uses a single entry point — {@code NovaxClient.execute(request)} —
 * so adding a new endpoint means writing one {@code XxxRequest} class plus its
 * matching response type; nothing else needs to change.
 */
public interface ApiRequest<R> {

    HttpMethod method();

    String path();

    default Map<String, ?> queryParams() {
        return Map.of();
    }

    default Map<String, String> headers() {
        return Map.of();
    }

    /**
     * Returns the request body (will be JSON-serialised), or {@code null}.
     */
    default Object body() {
        return null;
    }

    TypeRef<R> responseType();
}
