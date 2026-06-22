from typing import Any, Optional

from novax_sdk.pay.model import PayOrderAddressFixedResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class ReceiptAddressRequest(ApiRequest):
    """GET /pay/v3/receipt/address"""

    def __init__(self, protocol: Optional[str] = None,
                 smart_contract_address: Optional[str] = None,
                 company_user_id: Optional[str] = None) -> None:
        self._protocol = protocol
        self._smart_contract_address = smart_contract_address
        self._company_user_id = company_user_id

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/address"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "protocol": self._protocol,
            "smartContractAddress": self._smart_contract_address,
            "companyUserId": self._company_user_id,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderAddressFixedResp
