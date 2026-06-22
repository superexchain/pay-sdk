from __future__ import annotations

from dataclasses import dataclass, field
from decimal import Decimal
from typing import Optional


@dataclass
class PayProtocolsResp:
    currency: Optional[str] = None
    protocol: Optional[str] = None
    currencyType: Optional[int] = None
    chainName: Optional[str] = None
    smartContractAddress: Optional[str] = None


@dataclass
class PayOrderResp:
    shortName: Optional[str] = None
    logo: Optional[str] = None
    currency: Optional[str] = None
    currencyNumber: Optional[Decimal] = None
    payCurrencyNumber: Optional[Decimal] = None


@dataclass
class PayOrderAddressResp:
    protocol: Optional[str] = None
    currency: Optional[str] = None
    receiptAddress: Optional[str] = None
    endTime: Optional[int] = None
    status: Optional[int] = None


@dataclass
class PayOrderAddressFixedResp:
    protocol: Optional[str] = None
    receiptAddress: Optional[str] = None


@dataclass
class ReceiptHashOrderResp:
    id: Optional[int] = None
    userId: Optional[int] = None
    userCurrencyOrderId: Optional[int] = None
    companyOrderId: Optional[str] = None
    protocol: Optional[str] = None
    currency: Optional[str] = None
    fromAddress: Optional[str] = None
    toAddress: Optional[str] = None
    txId: Optional[str] = None
    amount: Optional[Decimal] = None
    status: Optional[int] = None
    successTime: Optional[int] = None
    createTime: Optional[int] = None
    type: Optional[int] = None
    fee: Optional[Decimal] = None


@dataclass
class ReceiptOrderResp:
    receiptOrderId: Optional[str] = None
    currency: Optional[str] = None
    protocol: Optional[str] = None
    smartContractAddress: Optional[str] = None
    currencyNumber: Optional[Decimal] = None
    acceptCurrencyNumber: Optional[Decimal] = None
    counts: Optional[int] = None
    okTime: Optional[int] = None
    orderStatus: Optional[int] = None
    status: Optional[int] = None
    endTime: Optional[int] = None
    loseTime: Optional[int] = None
    hashOrders: list[ReceiptHashOrderResp] = field(default_factory=list)


@dataclass
class ReceiptOrderAddressResp:
    receiptOrderId: Optional[str] = None
    currency: Optional[str] = None
    protocol: Optional[str] = None
    smartContractAddress: Optional[str] = None
    currencyNumber: Optional[Decimal] = None
    acceptCurrencyNumber: Optional[Decimal] = None
    counts: Optional[int] = None
    okTime: Optional[int] = None
    orderStatus: Optional[int] = None
    status: Optional[int] = None
    endTime: Optional[int] = None
    loseTime: Optional[int] = None
    hashOrders: list[ReceiptHashOrderResp] = field(default_factory=list)
    address: Optional[str] = None


@dataclass
class WithdrawOrderResp:
    withdrawOrderId: Optional[str] = None
    type: Optional[int] = None
    currency: Optional[str] = None
    protocol: Optional[str] = None
    smartContractAddress: Optional[str] = None
    address: Optional[str] = None
    currencyNumber: Optional[Decimal] = None
    status: Optional[int] = None
    hashOrders: list[ReceiptHashOrderResp] = field(default_factory=list)
