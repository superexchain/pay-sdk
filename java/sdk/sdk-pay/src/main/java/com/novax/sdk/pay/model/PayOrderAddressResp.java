package com.novax.sdk.pay.model;

import java.util.Date;

public record PayOrderAddressResp(
        String protocol,
        String currency,
        String receiptAddress,
        Date endTime,
        Integer status
) {
}
