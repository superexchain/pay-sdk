from dataclasses import dataclass
from typing import Any

from novax_sdk.pay.model import ReceiptOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class ReceiptOrdersRequest(ApiRequest):
    """GET /pay/v3/receipt/orders"""

    receipt_order_ids: str | None = None
    order_status: int | None = None
    status: int | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/orders"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "receiptOrderIds": self.receipt_order_ids,
            "orderStatus": self.order_status,
            "status": self.status,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return list[ReceiptOrderResp]
