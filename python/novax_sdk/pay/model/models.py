from __future__ import annotations

from dataclasses import dataclass, field
from decimal import Decimal


@dataclass
class PayProtocolsResp:
    currency: str | None = None
    protocol: str | None = None
    currency_type: int | None = None
    chain_name: str | None = None
    smart_contract_address: str | None = None


@dataclass
class PayOrderResp:
    short_name: str | None = None
    logo: str | None = None
    currency: str | None = None
    currency_number: Decimal | None = None
    pay_currency_number: Decimal | None = None


@dataclass
class PayOrderAddressResp:
    protocol: str | None = None
    currency: str | None = None
    receipt_address: str | None = None
    end_time: int | None = None
    status: int | None = None


@dataclass
class PayOrderAddressFixedResp:
    protocol: str | None = None
    receipt_address: str | None = None


@dataclass
class ReceiptHashOrderResp:
    id: int | None = None
    user_id: int | None = None
    user_currency_order_id: int | None = None
    company_order_id: str | None = None
    protocol: str | None = None
    currency: str | None = None
    from_address: str | None = None
    to_address: str | None = None
    tx_id: str | None = None
    amount: Decimal | None = None
    status: int | None = None
    success_time: int | None = None
    create_time: int | None = None
    type: int | None = None
    fee: Decimal | None = None


@dataclass
class ReceiptOrderResp:
    receipt_order_id: str | None = None
    currency: str | None = None
    protocol: str | None = None
    smart_contract_address: str | None = None
    currency_number: Decimal | None = None
    accept_currency_number: Decimal | None = None
    counts: int | None = None
    ok_time: int | None = None
    order_status: int | None = None
    status: int | None = None
    end_time: int | None = None
    lose_time: int | None = None
    hash_orders: list[ReceiptHashOrderResp] = field(default_factory=list)


@dataclass
class ReceiptOrderAddressResp:
    receipt_order_id: str | None = None
    currency: str | None = None
    protocol: str | None = None
    smart_contract_address: str | None = None
    currency_number: Decimal | None = None
    accept_currency_number: Decimal | None = None
    counts: int | None = None
    ok_time: int | None = None
    order_status: int | None = None
    status: int | None = None
    end_time: int | None = None
    lose_time: int | None = None
    hash_orders: list[ReceiptHashOrderResp] = field(default_factory=list)
    address: str | None = None


@dataclass
class WithdrawOrderResp:
    withdraw_order_id: str | None = None
    type: int | None = None
    currency: str | None = None
    protocol: str | None = None
    smart_contract_address: str | None = None
    address: str | None = None
    currency_number: Decimal | None = None
    status: int | None = None
    hash_orders: list[ReceiptHashOrderResp] = field(default_factory=list)
