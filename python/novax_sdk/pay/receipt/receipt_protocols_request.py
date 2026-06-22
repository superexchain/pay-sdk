from typing import Any, Optional

from novax_sdk.pay.model import PayProtocolsResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class ReceiptProtocolsRequest(ApiRequest):
    """GET /pay/v3/protocols — type: 1=dynamic address, 6=fixed address."""

    def __init__(self, type: int = 1) -> None:
        self._type = type

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/protocols"

    def query_params(self) -> dict[str, Any]:
        return {"type": self._type}

    @property
    def response_type(self):
        return list[PayProtocolsResp]
