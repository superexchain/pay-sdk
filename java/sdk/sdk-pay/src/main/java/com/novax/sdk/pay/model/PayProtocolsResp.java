package com.novax.sdk.pay.model;

public record PayProtocolsResp(
        String currency,
        String protocol,
        Integer currencyType,
        String chainName,
        String smartContractAddress
) {
}
