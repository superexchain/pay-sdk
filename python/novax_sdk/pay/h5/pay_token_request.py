from dataclasses import dataclass
from decimal import Decimal
from typing import Any

from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class PayTokenRequest(ApiRequest):
    """GET /pay/v3/token — fetches an H5 payment token.
    userId is resolved server-side from X-Access-Key; callers never supply it."""

    receipt_order_id: str
    currency_number: Decimal

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/token"

    def query_params(self) -> dict[str, Any]:
        return {
            "receiptOrderId": self.receipt_order_id,
            "currencyNumber": str(self.currency_number),
        }

    @property
    def response_type(self) -> type:
        return str
