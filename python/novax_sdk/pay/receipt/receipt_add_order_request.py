from dataclasses import dataclass
from decimal import Decimal
from typing import Any

from novax_sdk.pay.model import ReceiptOrderAddressResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class ReceiptAddOrderRequest(ApiRequest):
    """POST /pay/v3/receipt/order/add"""

    protocol: str | None = None
    currency: str | None = None
    smart_contract_address: str | None = None
    company_user_id: str | None = None
    receipt_order_id: str | None = None
    currency_number: Decimal | None = None
    call_back_url: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/order/add"

    def body(self) -> dict[str, Any]:
        return {
            "protocol": self.protocol,
            "currency": self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId": self.company_user_id,
            "receiptOrderId": self.receipt_order_id,
            "currencyNumber": str(self.currency_number) if self.currency_number else None,
            "callBackUrl": self.call_back_url,
        }

    @property
    def response_type(self) -> type:
        return ReceiptOrderAddressResp
