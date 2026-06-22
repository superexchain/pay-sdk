from decimal import Decimal
from typing import Any, Optional

from novax_sdk.request.api_request import ApiRequest, HttpMethod


class DynamicQrPayCreateRequest(ApiRequest):
    """POST /pay/v3/qr-code-pay/dynamic/create"""

    def __init__(self, receipt_order_id: str, company_user_id: int,
                 currency: str, amount: Decimal,
                 comment: Optional[str] = None) -> None:
        self._receipt_order_id = receipt_order_id
        self._company_user_id = company_user_id
        self._currency = currency
        self._amount = amount
        self._comment = comment

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/qr-code-pay/dynamic/create"

    def body(self) -> dict[str, Any]:
        return {
            "receiptOrderId": self._receipt_order_id,
            "companyUserId": self._company_user_id,
            "currency": self._currency,
            "amount": str(self._amount),
            "comment": self._comment,
        }

    @property
    def response_type(self) -> type:
        return str
