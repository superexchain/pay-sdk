package com.novax.sdk.pay.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Server-side this extends {@code ReceiptOrderResp}; flattened here since
 * the wire format is flat JSON.
 */
public record ReceiptOrderAddressResp(
        String receiptOrderId,
        String currency,
        String protocol,
        String smartContractAddress,
        BigDecimal currencyNumber,
        BigDecimal acceptCurrencyNumber,
        Long counts,
        Date okTime,
        Integer orderStatus,
        Integer status,
        Date endTime,
        Date loseTime,
        List<ReceiptHashOrderResp> hashOrders,
        String address
) {
}
