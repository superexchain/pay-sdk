package com.novax.sdk.pay.model;

import java.math.BigDecimal;

public record PayOrderResp(
        String shortName,
        String logo,
        String currency,
        BigDecimal currencyNumber,
        BigDecimal payCurrencyNumber
) {
}
