"""Integration tests for QRCodePayOrderApi endpoints."""
import time
from decimal import Decimal

from novax_sdk import NovaxClient
from novax_sdk.http.interceptors import LoggingInterceptor
from novax_sdk.pay.qrcode import DynamicQrPayCreateRequest

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


def test_dynamic_qr_pay_create():
    resp = CLIENT.execute(
        DynamicQrPayCreateRequest(
            receipt_order_id=str(int(time.time() * 1000)),
            company_user_id=88888896,
            currency="usdt",
            amount=Decimal("1"),
            comment="test",
        )
    )
    print(resp)
    assert resp is not None
    assert resp.code == 200
    assert resp.msg is not None and len(resp.msg) > 0


if __name__ == "__main__":
    test_dynamic_qr_pay_create()
