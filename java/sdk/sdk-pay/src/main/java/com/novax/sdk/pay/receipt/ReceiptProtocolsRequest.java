package com.novax.sdk.pay.receipt;

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
 * GET /pay/v3/protocols — list chains supported for receipt orders.
 * {@code type}: 1 = dynamic address, 6 = fixed address. Defaults to 1.
 */
@Getter
@Builder
public class ReceiptProtocolsRequest extends AbstractApiRequest<List<PayProtocolsResp>> {

    private final Integer type;

    @Override
    public HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    public String path() {
        return "/pay/v3/protocols";
    }

    @Override
    public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        q.put("type", type != null ? type : 1);
        return q;
    }

    @Override
    public TypeRef<List<PayProtocolsResp>> responseType() {
        return new TypeRef<>() {
        };
    }
}
