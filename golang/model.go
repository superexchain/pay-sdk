package novax

import "github.com/shopspring/decimal"

// ReturnResult is the server-side response envelope. code == 200 is success.
type ReturnResult[T any] struct {
	Code *int    `json:"code"`
	Msg  *string `json:"msg"`
	Data *T      `json:"data"`
}

func (r *ReturnResult[T]) IsSuccess() bool {
	return r.Code != nil && *r.Code == 200
}

// ── Response types ────────────────────────────────────────────────────────────

type PayProtocolsResp struct {
	Currency             string `json:"currency"`
	Protocol             string `json:"protocol"`
	CurrencyType         int    `json:"currencyType"`
	ChainName            string `json:"chainName"`
	SmartContractAddress string `json:"smartContractAddress"`
}

type PayOrderResp struct {
	ShortName         string          `json:"shortName"`
	Logo              string          `json:"logo"`
	Currency          string          `json:"currency"`
	CurrencyNumber    decimal.Decimal `json:"currencyNumber"`
	PayCurrencyNumber decimal.Decimal `json:"payCurrencyNumber"`
}

type PayOrderAddressResp struct {
	Protocol       string `json:"protocol"`
	Currency       string `json:"currency"`
	ReceiptAddress string `json:"receiptAddress"`
	EndTime        int64  `json:"endTime"`
	Status         int    `json:"status"`
}

type PayOrderAddressFixedResp struct {
	Protocol       string `json:"protocol"`
	ReceiptAddress string `json:"receiptAddress"`
}

type ReceiptHashOrderResp struct {
	ID                  int64           `json:"id"`
	UserID              int64           `json:"userId"`
	UserCurrencyOrderID int64           `json:"userCurrencyOrderId"`
	CompanyOrderID      string          `json:"companyOrderId"`
	Protocol            string          `json:"protocol"`
	Currency            string          `json:"currency"`
	FromAddress         string          `json:"fromAddress"`
	ToAddress           string          `json:"toAddress"`
	TxID                string          `json:"txId"`
	Amount              decimal.Decimal `json:"amount"`
	Status              int             `json:"status"`
	SuccessTime         int64           `json:"successTime"`
	CreateTime          int64           `json:"createTime"`
	Type                int             `json:"type"`
	Fee                 decimal.Decimal `json:"fee"`
}

type ReceiptOrderResp struct {
	ReceiptOrderID       string                 `json:"receiptOrderId"`
	Currency             string                 `json:"currency"`
	Protocol             string                 `json:"protocol"`
	SmartContractAddress string                 `json:"smartContractAddress"`
	CurrencyNumber       decimal.Decimal        `json:"currencyNumber"`
	AcceptCurrencyNumber decimal.Decimal        `json:"acceptCurrencyNumber"`
	Counts               int64                  `json:"counts"`
	OkTime               int64                  `json:"okTime"`
	OrderStatus          int                    `json:"orderStatus"`
	Status               int                    `json:"status"`
	EndTime              int64                  `json:"endTime"`
	LoseTime             int64                  `json:"loseTime"`
	HashOrders           []ReceiptHashOrderResp `json:"hashOrders"`
}

type ReceiptOrderAddressResp struct {
	ReceiptOrderID       string                 `json:"receiptOrderId"`
	Currency             string                 `json:"currency"`
	Protocol             string                 `json:"protocol"`
	SmartContractAddress string                 `json:"smartContractAddress"`
	CurrencyNumber       decimal.Decimal        `json:"currencyNumber"`
	AcceptCurrencyNumber decimal.Decimal        `json:"acceptCurrencyNumber"`
	Counts               int64                  `json:"counts"`
	OkTime               int64                  `json:"okTime"`
	OrderStatus          int                    `json:"orderStatus"`
	Status               int                    `json:"status"`
	EndTime              int64                  `json:"endTime"`
	LoseTime             int64                  `json:"loseTime"`
	HashOrders           []ReceiptHashOrderResp `json:"hashOrders"`
	Address              string                 `json:"address"`
}

type WithdrawOrderResp struct {
	WithdrawOrderID      string                 `json:"withdrawOrderId"`
	Type                 int                    `json:"type"`
	Currency             string                 `json:"currency"`
	Protocol             string                 `json:"protocol"`
	SmartContractAddress string                 `json:"smartContractAddress"`
	Address              string                 `json:"address"`
	CurrencyNumber       decimal.Decimal        `json:"currencyNumber"`
	Status               int                    `json:"status"`
	HashOrders           []ReceiptHashOrderResp `json:"hashOrders"`
}
