from typing import Any, Optional

from novax_sdk.pay.model import ReceiptOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class ReceiptOrdersRequest(ApiRequest):
    """GET /pay/v3/receipt/orders"""

    def __init__(self, receipt_order_ids: Optional[str] = None,
                 order_status: Optional[int] = None,
                 status: Optional[int] = None) -> None:
        self._receipt_order_ids = receipt_order_ids
        self._order_status = order_status
        self._status = status

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/orders"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "receiptOrderIds": self._receipt_order_ids,
            "orderStatus": self._order_status,
            "status": self._status,
        }.items() if v is not None}

    @property
    def response_type(self):
        return list[ReceiptOrderResp]
