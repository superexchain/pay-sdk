package com.novax.sdk.core.http;

import com.novax.sdk.core.json.JsonMapper;
import com.novax.sdk.core.request.HttpMethod;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SignatureCodecTest {

    private final JsonMapper mapper = JsonMapper.defaultMapper();

    @Test
    void getRequest_dataToSign_matchesServerCanonicalForm() {
        // Server-side SignatureUtil.generateDataToSign produces:
        //   METHOD&<sorted-query>&timestamp=<ts>
        URI uri = URI.create("https://api.novax.com/pay/v3/token?b=2&a=1&a=11");
        String got = SignatureCodec.dataToSign(HttpMethod.GET, uri, null, 1700000000000L, mapper);
        assertEquals("GET&a=1,11&b=2&timestamp=1700000000000", got);
    }

    @Test
    void postRequest_dataToSign_sortsBodyKeysAlphabetically() {
        URI uri = URI.create("https://api.novax.com/pay/v3/withdraw/order/add");
        byte[] body = "{\"chain\":\"TRC20\",\"amount\":100,\"address\":\"Txxx\"}"
                .getBytes(StandardCharsets.UTF_8);
        String got = SignatureCodec.dataToSign(HttpMethod.POST, uri, body, 1700000000000L, mapper);
        assertEquals("POST&address=Txxx&amount=100&chain=TRC20&timestamp=1700000000000", got);
    }

    @Test
    void hmacSha256Hex_isLowercase64Chars() {
        String sig = SignatureCodec.hmacSha256Hex("hello", "secret");
        assertEquals(64, sig.length());
        assertEquals(sig.toLowerCase(), sig);
        // Known value: HMAC-SHA256("hello", "secret")
        assertEquals("88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b", sig);
    }
}
