package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.PayProtocolsResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GET /pay/public/h5/protocols — list the protocols available for an H5 payment.
 */
@Getter
@Builder
public class H5ProtocolsRequest extends AbstractApiRequest<List<PayProtocolsResp>> {

    private final String token;

    @Override
    public HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    public String path() {
        return "/pay/public/h5/protocols";
    }

    @Override
    public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (token != null) q.put("token", token);
        return q;
    }

    @Override
    public TypeRef<List<PayProtocolsResp>> responseType() {
        return new TypeRef<>() {
        };
    }
}
