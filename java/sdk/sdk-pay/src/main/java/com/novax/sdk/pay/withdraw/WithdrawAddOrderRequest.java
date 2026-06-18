package com.novax.sdk.pay.withdraw;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * POST /pay/v3/withdraw/order/add — create a withdraw order.
 *
 * <p>{@code userId} is resolved server-side from the {@code X-Access-Key}
 * header by {@code SignatureFilter}; callers never supply it.
 */
@Getter
@Builder
public class WithdrawAddOrderRequest extends AbstractApiRequest<WithdrawAddOrderResponse> {

    private final BigDecimal amount;
    private final String chain;
    private final String address;

    @Override public HttpMethod method() { return HttpMethod.POST; }

    @Override public String path() { return "/pay/v3/withdraw/order/add"; }

    @Override public Object body() {
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("amount", amount);
        b.put("chain", chain);
        b.put("address", address);
        return b;
    }

    @Override public TypeRef<WithdrawAddOrderResponse> responseType() {
        return TypeRef.of(WithdrawAddOrderResponse.class);
    }
}
