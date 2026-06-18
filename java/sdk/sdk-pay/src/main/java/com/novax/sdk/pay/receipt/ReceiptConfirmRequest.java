package com.novax.sdk.pay.receipt;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/** POST /pay/v3/receipt/order/confirm — confirm a receipt order. */
@Getter
@Builder
public class ReceiptConfirmRequest extends AbstractApiRequest<Object> {

    private final String protocol;
    private final String currency;
    private final String smartContractAddress;
    private final String companyUserId;
    private final String receiptOrderId;

    @Override public HttpMethod method() { return HttpMethod.POST; }
    @Override public String path() { return "/pay/v3/receipt/order/confirm"; }

    @Override public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("protocol", protocol);
        b.put("currency", currency);
        b.put("smartContractAddress", smartContractAddress);
        b.put("companyUserId", companyUserId);
        b.put("receiptOrderId", receiptOrderId);
        return b;
    }

    @Override public TypeRef<Object> responseType() {
        return TypeRef.of(Object.class);
    }
}
