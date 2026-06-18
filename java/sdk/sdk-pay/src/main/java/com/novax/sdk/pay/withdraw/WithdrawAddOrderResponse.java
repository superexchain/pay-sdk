package com.novax.sdk.pay.withdraw;

import java.math.BigDecimal;

public record WithdrawAddOrderResponse(
        String orderId,
        String status,
        BigDecimal amount
) {
}
