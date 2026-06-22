from typing import Any, Optional

from novax_sdk.pay.model import WithdrawOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class WithdrawOrdersRequest(ApiRequest):
    """GET /pay/v3/withdraw/orders"""

    def __init__(self, withdraw_order_ids: Optional[str] = None,
                 status: Optional[int] = None) -> None:
        self._withdraw_order_ids = withdraw_order_ids
        self._status = status

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/withdraw/orders"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "withdrawOrderIds": self._withdraw_order_ids,
            "status": self._status,
        }.items() if v is not None}

    @property
    def response_type(self):
        return list[WithdrawOrderResp]
