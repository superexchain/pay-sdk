package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /pay/public/h5/ok-time — confirm the time at which payment was made.
 */
@Getter
@Builder
public class H5OkTimeRequest extends AbstractApiRequest<Boolean> {

    private final String protocol;
    private final String currency;
    private final String smartContractAddress;
    private final String companyUserId;
    private final String token;

    @Override
    public HttpMethod method() {
        return HttpMethod.POST;
    }

    @Override
    public String path() {
        return "/pay/public/h5/ok-time";
    }

    @Override
    public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("protocol", protocol);
        b.put("currency", currency);
        b.put("smartContractAddress", smartContractAddress);
        b.put("companyUserId", companyUserId);
        b.put("token", token);
        return b;
    }

    @Override
    public TypeRef<Boolean> responseType() {
        return TypeRef.of(Boolean.class);
    }
}
