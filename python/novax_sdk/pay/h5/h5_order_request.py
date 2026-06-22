from typing import Any, Optional

from novax_sdk.pay.model import PayOrderResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class H5OrderRequest(ApiRequest):
    """GET /pay/public/h5/order"""

    def __init__(self, token: Optional[str] = None) -> None:
        self._token = token

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/public/h5/order"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {"token": self._token}.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderResp
