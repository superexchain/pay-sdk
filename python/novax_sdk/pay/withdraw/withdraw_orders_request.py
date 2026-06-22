from dataclasses import dataclass
from typing import Any

from novax_sdk.pay.model import WithdrawOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class WithdrawOrdersRequest(ApiRequest):
    """GET /pay/v3/withdraw/orders"""

    withdraw_order_ids: str | None = None
    status: int | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/withdraw/orders"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "withdrawOrderIds": self.withdraw_order_ids,
            "status": self.status,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return list[WithdrawOrderResp]
