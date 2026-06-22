from dataclasses import dataclass
from typing import Any

from novax_sdk.pay.model import PayOrderAddressFixedResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class ReceiptAddressRequest(ApiRequest):
    """GET /pay/v3/receipt/address"""

    protocol: str | None = None
    smart_contract_address: str | None = None
    company_user_id: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/address"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "protocol": self.protocol,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId": self.company_user_id,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderAddressFixedResp
