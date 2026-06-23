"""Integration tests for WithdrawOrderApi endpoints."""
import time
from decimal import Decimal

from novax_sdk import NovaxClient
from novax_sdk.http.interceptors import LoggingInterceptor
from novax_sdk.pay.withdraw import WithdrawOrdersRequest, WithdrawAddOrderRequest

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


def test_withdraw_orders():
    resp = CLIENT.execute(WithdrawOrdersRequest(withdraw_order_ids="w_1234,1745564918818000"))
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_withdraw_add_order():
    resp = CLIENT.execute(
        WithdrawAddOrderRequest(
            withdraw_order_id=str(int(time.time() * 1000)),
            order_type=1,
            currency="USDT",
            protocol="TRC20",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            address="TVKUpYxUV4LTdFZ24kNrvMm6phXx6vv7Zc",
            currency_number=Decimal("1"),
            call_back_url="https://example.com/callback",
        )
    )
    print(resp)
    assert resp is not None
    assert resp.code == 200


if __name__ == "__main__":
    # test_withdraw_orders()
    test_withdraw_add_order()