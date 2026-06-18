package com.novax.sdk.core.http;

import com.novax.sdk.core.json.JsonMapper;
import com.novax.sdk.core.request.ApiRequest;
import com.novax.sdk.core.request.HttpMethod;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wire-level request — what the transport layer actually sends. Built from an
 * {@link ApiRequest} via {@link #from(URI, ApiRequest, JsonMapper)}; immutable
 * thereafter (interceptors get a fresh copy via {@link #withHeaders(Map)}).
 */
public record SdkRequest(
        HttpMethod method,
        URI uri,
        Map<String, String> headers,
        byte[] body
) {

    public static SdkRequest from(URI endpoint, ApiRequest<?> req, JsonMapper jsonMapper) {
        URI uri = buildUri(endpoint, req.path(), req.queryParams());
        Map<String, String> headers = new LinkedHashMap<>(req.headers());
        byte[] body = null;
        if (req.body() != null) {
            body = jsonMapper.writeBytes(req.body());
            headers.putIfAbsent("Content-Type", "application/json");
        }
        return new SdkRequest(req.method(), uri, Map.copyOf(headers), body);
    }

    public SdkRequest withHeaders(Map<String, String> extra) {
        Map<String, String> merged = new LinkedHashMap<>(headers);
        merged.putAll(extra);
        return new SdkRequest(method, uri, Map.copyOf(merged), body);
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
            if (!qs.isEmpty()) sb.append('?').append(qs);
        }
        return URI.create(sb.toString());
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
