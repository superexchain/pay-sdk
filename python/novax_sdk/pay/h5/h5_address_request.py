from dataclasses import dataclass
from typing import Any

from novax_sdk.pay.model import PayOrderAddressResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class H5AddressRequest(ApiRequest):
    """GET /pay/public/h5/address"""

    protocol: str | None = None
    currency: str | None = None
    smart_contract_address: str | None = None
    company_user_id: str | None = None
    token: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.GET

    @property
    def path(self) -> str:
        return "/pay/public/h5/address"

    def query_params(self) -> dict[str, Any]:
        return {k: v for k, v in {
            "protocol": self.protocol,
            "currency": self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId": self.company_user_id,
            "token": self.token,
        }.items() if v is not None}

    @property
    def response_type(self) -> type:
        return PayOrderAddressResp
