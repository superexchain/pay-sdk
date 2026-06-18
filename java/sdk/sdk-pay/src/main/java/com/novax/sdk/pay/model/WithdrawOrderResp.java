package com.novax.sdk.pay.model;

import java.math.BigDecimal;
import java.util.List;

public record WithdrawOrderResp(
        String withdrawOrderId,
        Integer type,
        String currency,
        String protocol,
        String smartContractAddress,
        String address,
        BigDecimal currencyNumber,
        Integer status,
        List<ReceiptHashOrderResp> hashOrders
) {
}
