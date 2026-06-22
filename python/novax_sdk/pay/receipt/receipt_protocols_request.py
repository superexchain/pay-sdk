from dataclasses import dataclass, field
from typing import Any

from novax_sdk.pay.model import PayProtocolsResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class ReceiptProtocolsRequest(ApiRequest):
    """GET /pay/v3/protocols — receipt_type: 1=dynamic address, 6=fixed address."""

    receipt_type: int = 1

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/protocols"

    def query_params(self) -> dict[str, Any]:
        return {"type": self.receipt_type}

    @property
    def response_type(self) -> type:
        return list[PayProtocolsResp]
