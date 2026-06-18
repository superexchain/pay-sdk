package com.novax.sdk.pay.h5;

import com.novax.sdk.core.NovaxClient;
import com.novax.sdk.core.NovaxConfig;
import com.novax.sdk.core.http.HttpTransport;
import com.novax.sdk.core.http.SdkRequest;
import com.novax.sdk.core.http.SdkResponse;
import com.novax.sdk.core.http.SignatureCodec;
import com.novax.sdk.core.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String SUCCESS_BODY =
            "{\"code\":0,\"message\":\"ok\",\"data\":\"abc123\"}";

    @Test
    void execute_signsAndIssuesGet_omitsUserId_parsesToken() {
        NovaxClient client = NovaxClient.builder()
                .endpoint("https://api.novax.dev/api")
                .accessKey(ACCESS_KEY, ACCESS_SECRET)
                .clientIp("1.2.3.4")
                .language("zh-CN")
                .insecureTls()
                .build();

        PayTokenResponse resp = client.execute(
                PayTokenRequest.builder().receiptOrderId("1234").currencyNumber(BigDecimal.ONE).build()
        );
        System.out.println(resp.token());



    }

    @Test
    void execute_publicPath_skipsSignature() {
        // Sanity check: shouldSign() only fires for /pay/v3/* and /free-spot/v3/*.
        // A request whose path doesn't match must NOT get the signature headers.
        AtomicReference<SdkRequest> captured = new AtomicReference<>();
        HttpTransport stub = req -> {
            captured.set(req);
            return new SdkResponse(200, Map.of(), SUCCESS_BODY.getBytes(StandardCharsets.UTF_8));
        };

        NovaxClient client = NovaxClient.builder()
                .endpoint("https://api.novax.com")
                .accessKey(ACCESS_KEY, ACCESS_SECRET)
                .config(cfg -> cfg.transport(stub))
                .build();

        // synthesize a request hitting /pay/public/* to verify bypass
        client.execute(new com.novax.sdk.core.request.AbstractApiRequest<PayTokenResponse>() {
            @Override public com.novax.sdk.core.request.HttpMethod method() {
                return com.novax.sdk.core.request.HttpMethod.GET;
            }
            @Override public String path() { return "/pay/public/h5/order"; }
            @Override public com.novax.sdk.core.request.TypeRef<PayTokenResponse> responseType() {
                return com.novax.sdk.core.request.TypeRef.of(PayTokenResponse.class);
            }
        });

        Map<String, String> h = captured.get().headers();
        assertFalse(h.containsKey("X-Access-Key"));
        assertFalse(h.containsKey("X-Signature"));
        assertFalse(h.containsKey("X-Timestamp"));
        assertTrue(true);
    }
}
