from decimal import Decimal
from typing import Any, Optional

from novax_sdk.pay.model import WithdrawOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class WithdrawAddOrderRequest(ApiRequest):
    """POST /pay/v3/withdraw/order/add"""

    def __init__(self, withdraw_order_id: Optional[str] = None,
                 type: Optional[int] = None,
                 currency: Optional[str] = None,
                 protocol: Optional[str] = None,
                 smart_contract_address: Optional[str] = None,
                 address: Optional[str] = None,
                 currency_number: Optional[Decimal] = None,
                 call_back_url: Optional[str] = None) -> None:
        self._withdraw_order_id = withdraw_order_id
        self._type = type
        self._currency = currency
        self._protocol = protocol
        self._smart_contract_address = smart_contract_address
        self._address = address
        self._currency_number = currency_number
        self._call_back_url = call_back_url

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/withdraw/order/add"

    def body(self) -> dict[str, Any]:
        return {
            "withdrawOrderId": self._withdraw_order_id,
            "type": self._type,
            "currency": self._currency,
            "protocol": self._protocol,
            "smartContractAddress": self._smart_contract_address,
            "address": self._address,
            "currencyNumber": str(self._currency_number) if self._currency_number else None,
            "callBackUrl": self._call_back_url,
        }

    @property
    def response_type(self) -> type:
        return WithdrawOrderResp
