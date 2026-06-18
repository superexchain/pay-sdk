package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.PayOrderAddressResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * GET /pay/public/h5/address — get the H5 payment receipt address.
 *
 * <p>Server expects {@code language} header (already injected by
 * {@code HeaderInterceptor} if configured on the client).
 */
@Getter
@Builder
public class H5AddressRequest extends AbstractApiRequest<PayOrderAddressResp> {

    private final String protocol;
    private final String currency;
    private final String smartContractAddress;
    private final String companyUserId;
    private final String token;

    @Override public HttpMethod method() { return HttpMethod.GET; }
    @Override public String path() { return "/pay/public/h5/address"; }

    @Override public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (protocol != null) q.put("protocol", protocol);
        if (currency != null) q.put("currency", currency);
        if (smartContractAddress != null) q.put("smartContractAddress", smartContractAddress);
        if (companyUserId != null) q.put("companyUserId", companyUserId);
        if (token != null) q.put("token", token);
        return q;
    }

    @Override public TypeRef<PayOrderAddressResp> responseType() {
        return TypeRef.of(PayOrderAddressResp.class);
    }
}
