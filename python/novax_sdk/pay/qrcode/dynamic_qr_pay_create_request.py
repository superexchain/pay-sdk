from dataclasses import dataclass
from decimal import Decimal
from typing import Any

from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class DynamicQrPayCreateRequest(ApiRequest):
    """POST /pay/v3/qr-code-pay/dynamic/create"""

    receipt_order_id: str
    company_user_id: int
    currency: str
    amount: Decimal
    comment: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/qr-code-pay/dynamic/create"

    def body(self) -> dict[str, Any]:
        return {
            "receiptOrderId": self.receipt_order_id,
            "companyUserId": self.company_user_id,
            "currency": self.currency,
            "amount": str(self.amount),
            "comment": self.comment,
        }

    @property
    def response_type(self) -> type:
        return str
