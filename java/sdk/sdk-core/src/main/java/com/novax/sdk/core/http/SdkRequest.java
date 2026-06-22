package com.novax.sdk.core.http;

import com.novax.sdk.core.json.JsonMapper;
import com.novax.sdk.core.request.ApiRequest;
import com.novax.sdk.core.request.HttpMethod;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wire-level request — what the transport layer actually sends. Built from an
 * {@link ApiRequest} via {@link #from(URI, ApiRequest, JsonMapper)}; immutable
 * thereafter.
 *
 * <p>{@code headers} is {@code Map<String, List<String>>} because HTTP allows
 * the same header name to appear multiple times (RFC 7230). Use
 * {@link #appendHeaders(Map)} to add additional values (multi-value semantics)
 * or {@link #setHeaders(Map)} to replace any existing values for the given keys.
 */
public record SdkRequest(
        HttpMethod method,
        URI uri,
        Map<String, List<String>> headers,
        byte[] body
) {

    public static SdkRequest from(URI endpoint, ApiRequest<?> req, JsonMapper jsonMapper) {
        URI uri = buildUri(endpoint, req.path(), req.queryParams());
        LinkedHashMap<String, List<String>> headers = new LinkedHashMap<>();
        for (var e : req.headers().entrySet()) {
            headers.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).add(e.getValue());
        }
        byte[] body = null;
        if (req.body() != null) {
            body = jsonMapper.writeBytes(req.body());
            headers.computeIfAbsent("Content-Type", k -> new ArrayList<>(List.of("application/json")));
        }
        return new SdkRequest(req.method(), uri, freeze(headers), body);
    }

    /**
     * Append each value to the list for its key (HTTP-correct multi-value).
     */
    public SdkRequest appendHeaders(Map<String, String> additional) {
        LinkedHashMap<String, List<String>> merged = mutableCopy(headers);
        for (var e : additional.entrySet()) {
            merged.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).add(e.getValue());
        }
        return new SdkRequest(method, uri, freeze(merged), body);
    }

    /**
     * Replace any existing values for these keys (single-valued semantics).
     */
    public SdkRequest setHeaders(Map<String, String> overrides) {
        LinkedHashMap<String, List<String>> merged = mutableCopy(headers);
        for (var e : overrides.entrySet()) {
            merged.put(e.getKey(), new ArrayList<>(List.of(e.getValue())));
        }
        return new SdkRequest(method, uri, freeze(merged), body);
    }

    private static LinkedHashMap<String, List<String>> mutableCopy(Map<String, List<String>> src) {
        LinkedHashMap<String, List<String>> copy = new LinkedHashMap<>();
        for (var e : src.entrySet()) {
            copy.put(e.getKey(), new ArrayList<>(e.getValue()));
        }
        return copy;
    }

    private static Map<String, List<String>> freeze(Map<String, List<String>> src) {
        LinkedHashMap<String, List<String>> copy = new LinkedHashMap<>();
        for (var e : src.entrySet()) {
            copy.put(e.getKey(), List.copyOf(e.getValue()));
        }
        return Map.copyOf(copy);
    }

    private static URI buildUri(URI base, String path, Map<String, ?> query) {
        StringBuilder sb = new StringBuilder(base.toString());
        if (sb.charAt(sb.length() - 1) == '/' && path.startsWith("/")) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(path);
        if (query != null && !query.isEmpty()) {
            String qs = query.entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> encode(e.getKey()) + "=" + encode(String.valueOf(e.getValue())))
                    .collect(Collectors.joining("&"));
            if (!qs.isEmpty()) {
                sb.append('?').append(qs);
            }
        }
        return URI.create(sb.toString());
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
