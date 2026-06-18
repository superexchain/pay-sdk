package com.novax.sdk.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public record ReceiptHashOrderResp(
        Long id,
        Long userId,
        Long userCurrencyOrderId,
        String companyOrderId,
        String protocol,
        String currency,
        String fromAddress,
        String toAddress,
        String txId,
        BigDecimal amount,
        Integer status,
        Date successTime,
        Date createTime,
        Integer type,
        BigDecimal fee
) {
}
