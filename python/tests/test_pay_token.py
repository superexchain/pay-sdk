"""Integration test — mirrors Java PayTokenRequestTest."""
import logging
from decimal import Decimal

from novax_sdk import NovaxClient
from novax_sdk.http.interceptors import LoggingInterceptor
from novax_sdk.pay.h5 import PayTokenRequest

logging.basicConfig(level=logging.INFO)

ACCESS_KEY = "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD"
ACCESS_SECRET = "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2"

CLIENT = (
    NovaxClient.builder()
    .endpoint("https://api.novax.dev/api")
    .access_key(ACCESS_KEY, ACCESS_SECRET)
    .client_ip("1.2.3.4")
    .language("zh-CN")
    .add_interceptor(LoggingInterceptor())
    .insecure_tls()
    .build()
)


def test_pay_token():
    resp = CLIENT.execute(
        PayTokenRequest(receipt_order_id="1234", currency_number=Decimal("1"))
    )
    print(resp)
    assert resp is not None
    assert resp.code == 200
    assert resp.data is not None and len(resp.data) > 0
