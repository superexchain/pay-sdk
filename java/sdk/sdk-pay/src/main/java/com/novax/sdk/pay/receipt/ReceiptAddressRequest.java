package com.novax.sdk.pay.receipt;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.PayOrderAddressFixedResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/** GET /pay/v3/receipt/address — fixed receipt address lookup. */
@Getter
@Builder
public class ReceiptAddressRequest extends AbstractApiRequest<PayOrderAddressFixedResp> {

    private final String protocol;
    private final String smartContractAddress;
    private final String companyUserId;

    @Override public HttpMethod method() { return HttpMethod.GET; }
    @Override public String path() { return "/pay/v3/receipt/address"; }

    @Override public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (protocol != null) q.put("protocol", protocol);
        if (smartContractAddress != null) q.put("smartContractAddress", smartContractAddress);
        if (companyUserId != null) q.put("companyUserId", companyUserId);
        return q;
    }

    @Override public TypeRef<PayOrderAddressFixedResp> responseType() {
        return TypeRef.of(PayOrderAddressFixedResp.class);
    }
}
