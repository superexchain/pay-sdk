package com.novax.sdk.pay.receipt;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.ReceiptOrderAddressResp;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** POST /pay/v3/receipt/order/add — create a receipt order. */
@Getter
@Builder
public class ReceiptAddOrderRequest extends AbstractApiRequest<ReceiptOrderAddressResp> {

    private final String protocol;
    private final String currency;
    private final String smartContractAddress;
    private final String companyUserId;
    private final String receiptOrderId;
    private final BigDecimal currencyNumber;
    private final String callBackUrl;

    @Override public HttpMethod method() { return HttpMethod.POST; }
    @Override public String path() { return "/pay/v3/receipt/order/add"; }

    @Override public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("protocol", protocol);
        b.put("currency", currency);
        b.put("smartContractAddress", smartContractAddress);
        b.put("companyUserId", companyUserId);
        b.put("receiptOrderId", receiptOrderId);
        b.put("currencyNumber", currencyNumber);
        b.put("callBackUrl", callBackUrl);
        return b;
    }

    @Override public TypeRef<ReceiptOrderAddressResp> responseType() {
        return TypeRef.of(ReceiptOrderAddressResp.class);
    }
}
