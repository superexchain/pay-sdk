from dataclasses import dataclass
from typing import Any

from novax_sdk.pay.model import PayOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class H5OrderRequest(ApiRequest):
    """GET /pay/public/h5/order"""

    token: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/public/h5/order"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {"token": self.token}.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderResp
