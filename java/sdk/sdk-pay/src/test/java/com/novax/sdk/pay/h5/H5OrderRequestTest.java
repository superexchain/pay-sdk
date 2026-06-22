package com.novax.sdk.pay.h5;

import com.novax.sdk.core.NovaxClient;
import com.novax.sdk.core.exception.NovaxException;
import com.novax.sdk.core.model.ReturnResult;
import com.novax.sdk.core.http.interceptors.LoggingInterceptor;
import com.novax.sdk.pay.model.PayOrderResp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H5OrderRequestTest {

    private static final String ACCESS_KEY = "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD";
    private static final String ACCESS_SECRET = "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2";

    private static final NovaxClient CLIENT = NovaxClient.builder()
            .endpoint("https://api.novax.dev/api")
            .accessKey(ACCESS_KEY, ACCESS_SECRET)
            .clientIp("1.2.3.4")
            .language("zh-CN")
            .addInterceptor(new LoggingInterceptor())
            .insecureTls()
            .build();

    @Test
    void execute_h5Order() {
        ReturnResult<PayTokenResponse> resp1 = CLIENT.execute(
                PayTokenRequest.builder().receiptOrderId(String.valueOf(System.currentTimeMillis())).currencyNumber(BigDecimal.ONE).build());
        ReturnResult<PayOrderResp> resp = CLIENT.execute(H5OrderRequest.builder().token(resp1.data().token()).build());
        System.out.println(resp);
        assertNotNull(resp);
    }
}
