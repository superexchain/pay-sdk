from decimal import Decimal
from typing import Any, Optional

from novax_sdk.pay.model import ReceiptOrderAddressResp
from novax_sdk.request.api_request import ApiRequest, HttpMethod


class ReceiptAddOrderRequest(ApiRequest):
    """POST /pay/v3/receipt/order/add"""

    def __init__(self, protocol: Optional[str] = None, currency: Optional[str] = None,
                 smart_contract_address: Optional[str] = None,
                 company_user_id: Optional[str] = None,
                 receipt_order_id: Optional[str] = None,
                 currency_number: Optional[Decimal] = None,
                 call_back_url: Optional[str] = None) -> None:
        self._protocol = protocol
        self._currency = currency
        self._smart_contract_address = smart_contract_address
        self._company_user_id = company_user_id
        self._receipt_order_id = receipt_order_id
        self._currency_number = currency_number
        self._call_back_url = call_back_url

    @property
    def method(self) -> HttpMethod:
        return HttpMethod.POST

    @property
    def path(self) -> str:
        return "/pay/v3/receipt/order/add"

    def body(self) -> dict[str, Any]:
        return {
            "protocol": self._protocol,
            "currency": self._currency,
            "smartContractAddress": self._smart_contract_address,
            "companyUserId": self._company_user_id,
            "receiptOrderId": self._receipt_order_id,
            "currencyNumber": str(self._currency_number) if self._currency_number else None,
            "callBackUrl": self._call_back_url,
        }

    @property
    def response_type(self) -> type:
        return ReceiptOrderAddressResp
