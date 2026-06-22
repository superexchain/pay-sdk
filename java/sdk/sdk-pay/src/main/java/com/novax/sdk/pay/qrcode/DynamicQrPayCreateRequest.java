package com.novax.sdk.pay.qrcode;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /pay/v3/qr-code-pay/dynamic/create — create a dynamic QR-code order.
 * Returns the order id (or token) as a raw string.
 */
@Getter
@Builder
public class DynamicQrPayCreateRequest extends AbstractApiRequest<String> {

    private final String receiptOrderId;
    private final Long companyUserId;
    private final String currency;
    private final BigDecimal amount;
    private final String comment;

    @Override
    public HttpMethod method() {
        return HttpMethod.POST;
    }

    @Override
    public String path() {
        return "/pay/v3/qr-code-pay/dynamic/create";
    }

    @Override
    public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("receiptOrderId", receiptOrderId);
        b.put("companyUserId", companyUserId);
        b.put("currency", currency);
        b.put("amount", amount);
        b.put("comment", comment);
        return b;
    }

    @Override
    public TypeRef<String> responseType() {
        return TypeRef.of(String.class);
    }
}
