from dataclasses import dataclass
from typing import Any

from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class ReceiptConfirmRequest(ApiRequest):
    """POST /pay/v3/receipt/order/confirm"""

    protocol: str | None = None
    currency: str | None = None
    smart_contract_address: str | None = None
    company_user_id: str | None = None
    receipt_order_id: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/order/confirm"

    def body(self) -> dict[str, Any]:
        return {
            "protocol": self.protocol,
            "currency": self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId": self.company_user_id,
            "receiptOrderId": self.receipt_order_id,
        }

    @property
    def response_type(self) -> None:
        return None
