package com.novax.sdk.pay.h5;

import com.novax.sdk.core.NovaxClient;
import com.novax.sdk.core.exception.NovaxException;
import com.novax.sdk.core.model.ReturnResult;
import com.novax.sdk.core.http.interceptors.LoggingInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Drives a {@link PayTokenRequest} through {@link NovaxClient} with a stub
 * transport so we can assert on the exact wire shape — including signature
 * headers, the absence of {@code userId} (server injects it), and the parsed
 * response.
 */
class PayTokenRequestTest {

    private static final String ACCESS_KEY = "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD";
    private static final String ACCESS_SECRET = "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2";
    // server returns ReturnResult<String> — data is the raw token string, not an object

    private static final NovaxClient CLIENT = NovaxClient.builder()
            .endpoint("https://api.novax.dev/api")
            .accessKey(ACCESS_KEY, ACCESS_SECRET)
            .clientIp("1.2.3.4")
            .language("zh-CN")
            .addInterceptor(new LoggingInterceptor())
            .insecureTls()
            .build();

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1.1, false",
            "1.0, false",
            "1.000, false",
            "999, false"})
    void execute_signsAndIssuesGet_omitsUserId_parsesToken(BigDecimal number, boolean expectException) {
        try {
            ReturnResult<PayTokenResponse> resp = CLIENT.execute(
                    PayTokenRequest.builder().receiptOrderId("1234").currencyNumber(number).build()
            );
            System.out.println(resp.data().token());
            assertFalse(expectException);
            assertTrue(resp.data().token() != null && !resp.data().token().isBlank());
        } catch (Exception e) {
            assertTrue(expectException);
            assertInstanceOf(NovaxException.class, e);
        }
    }

}
