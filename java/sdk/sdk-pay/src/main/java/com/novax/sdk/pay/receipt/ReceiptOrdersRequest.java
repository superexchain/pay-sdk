package com.novax.sdk.pay.receipt;

import com.novax.sdk.core.request.AbstractApiRequest;
import com.novax.sdk.core.request.HttpMethod;
import com.novax.sdk.core.request.TypeRef;
import com.novax.sdk.pay.model.ReceiptOrderResp;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** GET /pay/v3/receipt/orders — query receipt orders. */
@Getter
@Builder
public class ReceiptOrdersRequest extends AbstractApiRequest<List<ReceiptOrderResp>> {

    private final String receiptOrderIds;
    private final Integer orderStatus;
    private final Integer status;

    @Override public HttpMethod method() { return HttpMethod.GET; }
    @Override public String path() { return "/pay/v3/receipt/orders"; }

    @Override public Map<String, ?> queryParams() {
        Map<String, Object> q = new LinkedHashMap<>();
        if (receiptOrderIds != null) q.put("receiptOrderIds", receiptOrderIds);
        if (orderStatus != null) q.put("orderStatus", orderStatus);
        if (status != null) q.put("status", status);
        return q;
    }

    @Override public TypeRef<List<ReceiptOrderResp>> responseType() {
        return new TypeRef<>() {};
    }
}
