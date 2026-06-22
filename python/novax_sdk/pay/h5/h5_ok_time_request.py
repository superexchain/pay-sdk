from dataclasses import dataclass
from typing import Any

from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class H5OkTimeRequest(ApiRequest):
    """POST /pay/public/h5/ok-time"""

    protocol: str | None = None
    currency: str | None = None
    smart_contract_address: str | None = None
    company_user_id: str | None = None
    token: str | None = None

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/public/h5/ok-time"

    def body(self) -> dict[str, Any]:
        return {
            "protocol": self.protocol,
            "currency": self.currency,
            "smartContractAddress": self.smart_contract_address,
            "companyUserId": self.company_user_id,
            "token": self.token,
        }

    @property
    def response_type(self) -> type:
        return bool
