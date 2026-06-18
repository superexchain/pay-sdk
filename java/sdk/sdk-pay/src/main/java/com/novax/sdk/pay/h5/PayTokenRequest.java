package com.novax.sdk.pay.h5;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GET /pay/v3/token — fetches an H5 payment token.
 *
 * <p>{@code userId} is resolved server-side from the {@code X-Access-Key}
 * header by {@code SignatureFilter}; callers never supply it.
 */
@Getter
@Builder
public class PayTokenRequest extends AbstractApiRequest<PayTokenResponse> {

    private final String receiptOrderId;

    private final BigDecimal currencyNumber;

    @Override
    public HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    public String path() {
        return "/pay/v3/token";
    }

    @Override
    public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        assert Objects.nonNull(receiptOrderId);
        assert Objects.nonNull(currencyNumber);
        q.put("receiptOrderId", receiptOrderId);
        q.put("currencyNumber", currencyNumber);
        return q;
    }

    @Override
    public TypeRef<PayTokenResponse> responseType() {
        return TypeRef.of(PayTokenResponse.class);
    }
}
