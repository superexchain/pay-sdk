"""Integration tests for ReceiptOrderApi endpoints."""
import time
from decimal import Decimal

from novax_sdk import NovaxClient
from novax_sdk.http.interceptors import LoggingInterceptor
from novax_sdk.pay.receipt import (
    ReceiptProtocolsRequest,
    ReceiptAddressRequest,
    ReceiptOrdersRequest,
    ReceiptAddOrderRequest,
    ReceiptConfirmRequest,
)

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


def test_receipt_protocols_dynamic():
    resp = CLIENT.execute(ReceiptProtocolsRequest(receipt_type=1))
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_receipt_protocols_fixed():
    resp = CLIENT.execute(ReceiptProtocolsRequest(receipt_type=6))
    print(resp)
    assert resp is not None


def test_receipt_address():
    resp = CLIENT.execute(
        ReceiptAddressRequest(
            protocol="TRC20",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
        )
    )
    print(resp)
    assert resp is not None


def test_receipt_orders():
    resp = CLIENT.execute(ReceiptOrdersRequest())
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_receipt_add_order():
    resp = CLIENT.execute(
        ReceiptAddOrderRequest(
            protocol="TRC20",
            currency="USDT",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            receipt_order_id=str(int(time.time() * 1000)),
            currency_number=Decimal("1"),
            call_back_url="http://tripartite-payment-ts.dev.svc.cluster.local/v3/public/test/call/back",
        )
    )
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_receipt_confirm():
    resp = CLIENT.execute(
        ReceiptConfirmRequest(
            protocol="TRC20",
            currency="usdt",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            receipt_order_id="1234",
        )
    )
    print(resp)
    assert resp is not None


if __name__ == "__main__":
    # test_receipt_protocols_dynamic()
    # test_receipt_protocols_fixed()
    # test_receipt_address()
    # test_receipt_orders()
    # test_receipt_add_order()
    test_receipt_confirm()