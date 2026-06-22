from typing import Any, Optional

from novax_sdk.pay.model import PayOrderAddressResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class H5OrderStatusRequest(ApiRequest):
    """GET /pay/public/h5/order/pay/status"""

    def __init__(self, protocol: Optional[str] = None, currency: Optional[str] = None,
                 smart_contract_address: Optional[str] = None,
                 company_user_id: Optional[str] = None,
                 token: Optional[str] = None) -> None:
        self._protocol = protocol
        self._currency = currency
        self._smart_contract_address = smart_contract_address
        self._company_user_id = company_user_id
        self._token = token

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/public/h5/order/pay/status"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "protocol": self._protocol,
            "currency": self._currency,
            "smartContractAddress": self._smart_contract_address,
            "companyUserId": self._company_user_id,
            "token": self._token,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderAddressResp
