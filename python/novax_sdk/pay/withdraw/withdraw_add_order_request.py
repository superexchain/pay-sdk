from dataclasses import dataclass
from decimal import Decimal
from typing import Any

from novax_sdk.pay.model import WithdrawOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class WithdrawAddOrderRequest(ApiRequest):
    """POST /pay/v3/withdraw/order/add"""

    withdraw_order_id: str | None = None
    order_type: int | None = None
    currency: str | None = None
    protocol: str | None = None
    smart_contract_address: str | None = None
    address: str | None = None
    currency_number: Decimal | None = None
    call_back_url: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/withdraw/order/add"

    def body(self) -> dict[str, Any]:
        return {
            "withdrawOrderId": self.withdraw_order_id,
            "type": self.order_type,
            "currency": self.currency,
            "protocol": self.protocol,
            "smartContractAddress": self.smart_contract_address,
            "address": self.address,
            "currencyNumber": str(self.currency_number) if self.currency_number else None,
            "callBackUrl": self.call_back_url,
        }

    @property
    def response_type(self) -> type:
        return WithdrawOrderResp
