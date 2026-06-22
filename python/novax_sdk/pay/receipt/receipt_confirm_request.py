from typing import Any, Optional

from novax_sdk.request.api_request import ApiRequest, HttpMethod


class ReceiptConfirmRequest(ApiRequest):
    """POST /pay/v3/receipt/order/confirm"""

    def __init__(self, protocol: Optional[str] = None, currency: Optional[str] = None,
                 smart_contract_address: Optional[str] = None,
                 company_user_id: Optional[str] = None,
                 receipt_order_id: Optional[str] = None) -> None:
        self._protocol = protocol
        self._currency = currency
        self._smart_contract_address = smart_contract_address
        self._company_user_id = company_user_id
        self._receipt_order_id = receipt_order_id

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/order/confirm"

    def body(self) -> dict[str, Any]:
        return {
            "protocol": self._protocol,
            "currency": self._currency,
            "smartContractAddress": self._smart_contract_address,
            "companyUserId": self._company_user_id,
            "receiptOrderId": self._receipt_order_id,
        }

    @property
    def response_type(self):
        return None
