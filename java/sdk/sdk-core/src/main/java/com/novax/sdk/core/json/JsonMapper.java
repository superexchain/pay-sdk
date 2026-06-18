package com.novax.sdk.core.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novax.sdk.core.exception.NovaxException;
import com.novax.sdk.core.model.ReturnResult;
import com.novax.sdk.core.request.TypeRef;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

/** Thin Jackson wrapper. Hidden behind a facade so the rest of the SDK never
 *  imports {@code ObjectMapper} directly. */
public final class JsonMapper {

    private final ObjectMapper mapper;

    public JsonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public static JsonMapper defaultMapper() {
        ObjectMapper m = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .findAndRegisterModules();
        return new JsonMapper(m);
    }

    public byte[] writeBytes(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new NovaxException("failed to serialise request body", e);
        }
    }

    public <T> ReturnResult<T> readReturnResult(byte[] body, TypeRef<T> dataType) {
        try {
            JavaType inner = mapper.getTypeFactory().constructType(dataType.type());
            JavaType outer = mapper.getTypeFactory()
                    .constructParametricType(ReturnResult.class, inner);
            return mapper.readValue(body, outer);
        } catch (IOException e) {
            throw new NovaxException("failed to parse ReturnResult", e);
        }
    }

    /**
     * Canonicalises a JSON body for HMAC signing — mirrors the server-side
     * {@code SignatureUtil#getSortedBodyString}. Top-level keys are sorted
     * alphabetically; primitives are rendered with their plain text, nested
     * objects/arrays are rendered as compact JSON.
     */
    public String sortedBodyForSigning(byte[] body) {
        if (body == null || body.length == 0) return null;
        try {
            JsonNode root = mapper.readTree(body);
            if (root.isArray()) {
                StringBuilder sb = new StringBuilder();
                for (JsonNode element : root) {
                    if (sb.length() > 0) sb.append('&');
                    sb.append(sortObject(element));
                }
                return sb.toString();
            }
            return sortObject(root);
        } catch (IOException e) {
            throw new NovaxException("failed to canonicalise body for signing", e);
        }
    }

    private static String sortObject(JsonNode node) {
        if (!node.isObject()) return node.asText();
        TreeMap<String, JsonNode> sorted = new TreeMap<>();
        Iterator<String> names = node.fieldNames();
        while (names.hasNext()) {
            String name = names.next();
            sorted.put(name, node.get(name));
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (var e : sorted.entrySet()) {
            if (!first) sb.append('&');
            first = false;
            sb.append(e.getKey()).append('=').append(renderValue(e.getValue()));
        }
        return sb.toString();
    }

    private static String renderValue(JsonNode v) {
        if (v.isNull()) return "";
        if (v.isTextual()) return v.asText();
        if (v.isNumber() || v.isBoolean()) return v.asText();
        return v.toString();
    }
}
