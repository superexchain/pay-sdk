package com.novax.sdk.pay.withdraw;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.WithdrawOrderResp;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /pay/v3/withdraw/order/add — create a withdraw order.
 */
@Getter
@Builder
public class WithdrawAddOrderRequest extends AbstractApiRequest<WithdrawOrderResp> {

    private final String withdrawOrderId;
    private final Integer type;
    private final String currency;
    private final String protocol;
    private final String smartContractAddress;
    private final String address;
    private final BigDecimal currencyNumber;
    private final String callBackUrl;

    @Override
    public HttpMethod method() {
        return HttpMethod.POST;
    }

    @Override
    public String path() {
        return "/pay/v3/withdraw/order/add";
    }

    @Override
    public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("withdrawOrderId", withdrawOrderId);
        b.put("type", type);
        b.put("currency", currency);
        b.put("protocol", protocol);
        b.put("smartContractAddress", smartContractAddress);
        b.put("address", address);
        b.put("currencyNumber", currencyNumber);
        b.put("callBackUrl", callBackUrl);
        return b;
    }

    @Override
    public TypeRef<WithdrawOrderResp> responseType() {
        return TypeRef.of(WithdrawOrderResp.class);
    }
}
