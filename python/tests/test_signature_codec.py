"""Mirrors Java SignatureCodecTest — verifies canonical form matches server-side logic."""
import pytest
from novax_sdk.http.signature_codec import SignatureCodec
from novax_sdk.request.api_request import HttpMethod


def test_get_request_sorted_query():
    url = "https://api.novax.com/pay/v3/token?b=2&a=1&a=11"
    result = SignatureCodec.data_to_sign(HttpMethod.GET, url, None, 1700000000000)
    assert result == "GET&a=1,11&b=2&timestamp=1700000000000"


def test_post_request_sorted_body():
    import json
    url = "https://api.novax.com/pay/v3/withdraw/order/add"
    body = json.dumps({"chain": "TRC20", "amount": 100, "address": "Txxx"}).encode()
    result = SignatureCodec.data_to_sign(HttpMethod.POST, url, body, 1700000000000)
    assert result == "POST&address=Txxx&amount=100&chain=TRC20&timestamp=1700000000000"


def test_hmac_sha256_hex():
    sig = SignatureCodec.hmac_sha256_hex("hello", "secret")
    assert len(sig) == 64
    assert sig == sig.lower()
    assert sig == "88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b"
