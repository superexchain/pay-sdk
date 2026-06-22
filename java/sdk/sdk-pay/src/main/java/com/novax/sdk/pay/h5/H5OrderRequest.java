package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.PayOrderResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /pay/public/h5/order — fetch the H5 payment order summary by token.
 */
@Getter
@Builder
public class H5OrderRequest extends AbstractApiRequest<PayOrderResp> {

    private final String token;

    @Override
    public HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    public String path() {
        return "/pay/public/h5/order";
    }

    @Override
    public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (token != null) q.put("token", token);
        return q;
    }

    @Override
    public TypeRef<PayOrderResp> responseType() {
        return TypeRef.of(PayOrderResp.class);
    }
}
