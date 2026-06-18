package com.novax.sdk.pay.withdraw;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.WithdrawOrderResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** GET /pay/v3/withdraw/orders — query withdraw orders. */
@Getter
@Builder
public class WithdrawOrdersRequest extends AbstractApiRequest<List<WithdrawOrderResp>> {

    private final String withdrawOrderIds;
    private final Integer status;

    @Override public HttpMethod method() { return HttpMethod.GET; }
    @Override public String path() { return "/pay/v3/withdraw/orders"; }

    @Override public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (withdrawOrderIds != null) q.put("withdrawOrderIds", withdrawOrderIds);
        if (status != null) q.put("status", status);
        return q;
    }

    @Override public TypeRef<List<WithdrawOrderResp>> responseType() {
        return new TypeRef<>() {};
    }
}
