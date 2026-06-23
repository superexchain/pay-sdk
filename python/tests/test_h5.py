"""Integration tests for PayH5Api endpoints."""
from decimal import Decimal

from novax_sdk import NovaxClient
from novax_sdk.http.interceptors import LoggingInterceptor
from novax_sdk.pay.h5 import (
    PayTokenRequest,
    H5OrderRequest,
    H5ProtocolsRequest,
    H5AddressRequest,
    H5OkTimeRequest,
    H5ConfirmRequest,
    H5OrderStatusRequest,
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


def _token() -> str:
    """Fetch a fresh token — used as prerequisite by downstream h5 tests."""
    resp = CLIENT.execute(
        PayTokenRequest(receipt_order_id="1234", currency_number=Decimal("1"))
    )
    assert resp.code == 200
    return resp.data


def test_h5_order():
    token = _token()
    resp = CLIENT.execute(H5OrderRequest(token=token))
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_h5_protocols():
    token = _token()
    resp = CLIENT.execute(H5ProtocolsRequest(token=token))
    print(resp)
    assert resp is not None
    assert resp.code == 200


def test_h5_address():
    token = _token()
    resp = CLIENT.execute(
        H5AddressRequest(
            protocol="TRC20",
            currency="USDT",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            token=token,
        )
    )
    print(resp)
    assert resp is not None


def test_h5_ok_time():
    token = _token()
    resp = CLIENT.execute(
        H5OkTimeRequest(
            protocol="TRC20",
            currency="USDT",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            token=token,
        )
    )
    print(resp)
    assert resp is not None


def test_h5_confirm():
    token = _token()
    resp = CLIENT.execute(
        H5ConfirmRequest(
            protocol="TRC20",
            currency="USDT",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            token=token,
        )
    )
    print(resp)
    assert resp is not None


def test_h5_order_status():
    token = _token()
    resp = CLIENT.execute(
        H5OrderStatusRequest(
            protocol="TRC20",
            currency="USDT",
            smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            company_user_id="88888896",
            token=token,
        )
    )
    print(resp)
    assert resp is not None

if __name__ == "__main__":
    # test_h5_order()
    # test_h5_protocols()
    # test_h5_address()
    # test_h5_ok_time()
    # test_h5_confirm()
    test_h5_order_status()