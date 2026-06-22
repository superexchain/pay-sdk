package com.novax.sdk.core.http;

import com.novax.sdk.core.exception.NovaxException;
import com.novax.sdk.core.json.JsonMapper;
import com.novax.sdk.core.request.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Mirrors the server-side {@code SignatureUtil}. Builds the canonical
 * data-to-sign string and computes the HMAC-SHA256 hex digest.
 *
 * <pre>
 *   METHOD[&amp;sorted-query][&amp;sorted-body]&amp;timestamp=&lt;ms&gt;
 * </pre>
 *
 * <p>Query: keys are URL-decoded then sorted alphabetically; multi-value keys
 * join their values with {@code ,}. Body: parsed as JSON, top-level keys
 * sorted; primitive values rendered with {@code toString()}, nested
 * objects/arrays rendered as compact JSON.
 */
public final class SignatureCodec {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private SignatureCodec() {
    }

    public static String dataToSign(HttpMethod method, URI uri, byte[] body,
                                    long timestampMs, JsonMapper jsonMapper) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.name().toUpperCase());

        String sortedQuery = sortedQuery(uri);
        if (!sortedQuery.isEmpty()) {
            sb.append('&').append(sortedQuery);
        }
        String sortedBody = jsonMapper.sortedBodyForSigning(body);
        if (sortedBody != null && !sortedBody.isEmpty()) {
            sb.append('&').append(sortedBody);
        }
        sb.append("&timestamp=").append(timestampMs);
        return sb.toString();
    }

    public static String hmacSha256Hex(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (GeneralSecurityException e) {
            throw new NovaxException("HMAC-SHA256 computation failed", e);
        }
    }

    private static String sortedQuery(URI uri) {
        String raw = uri.getRawQuery();
        if (raw == null || raw.isEmpty()) return "";
        Map<String, List<String>> grouped = new TreeMap<>();
        for (String pair : raw.split("&")) {
            if (pair.isEmpty()) continue;
            int eq = pair.indexOf('=');
            String key = (eq < 0) ? pair : pair.substring(0, eq);
            String value = (eq < 0) ? "" : pair.substring(eq + 1);
            grouped.computeIfAbsent(decode(key), k -> new ArrayList<>()).add(decode(value));
        }
        return grouped.entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining("&"));
    }

    private static String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}
