package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /pay/public/h5/confirm — user clicks "confirm" on the H5 pay page.
 */
@Getter
@Builder
public class H5ConfirmRequest extends AbstractApiRequest<Object> {

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
        return "/pay/public/h5/confirm";
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
    public TypeRef<Object> responseType() {
        return TypeRef.of(Object.class);
    }
}
