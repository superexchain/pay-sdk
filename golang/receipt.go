package novax

import (
	"net/http"
	"net/url"
	"strconv"

	"github.com/shopspring/decimal"
)

// ReceiptProtocolsRequest lists supported chains.
// ReceiptType: 1 = dynamic address, 6 = fixed address (defaults to 1).
type ReceiptProtocolsRequest struct{ ReceiptType int }

func (r *ReceiptProtocolsRequest) Method() string     { return http.MethodGet }
func (r *ReceiptProtocolsRequest) Path() string       { return "/pay/v3/protocols" }
func (r *ReceiptProtocolsRequest) Extra() http.Header { return nil }
func (r *ReceiptProtocolsRequest) Body() any          { return nil }
func (r *ReceiptProtocolsRequest) Query() url.Values {
	t := r.ReceiptType
	if t == 0 {
		t = 1
	}
	return url.Values{"type": {strconv.Itoa(t)}}
}

// ReceiptAddressRequest looks up a fixed receipt address.
type ReceiptAddressRequest struct {
	Protocol             string
	SmartContractAddress string
	CompanyUserID        string
}

func (r *ReceiptAddressRequest) Method() string     { return http.MethodGet }
func (r *ReceiptAddressRequest) Path() string       { return "/pay/v3/receipt/address" }
func (r *ReceiptAddressRequest) Extra() http.Header { return nil }
func (r *ReceiptAddressRequest) Body() any          { return nil }
func (r *ReceiptAddressRequest) Query() url.Values {
	q := url.Values{}
	setQ(q, "protocol", r.Protocol)
	setQ(q, "smartContractAddress", r.SmartContractAddress)
	setQ(q, "companyUserId", r.CompanyUserID)
	return q
}

// ReceiptOrdersRequest queries receipt orders.
type ReceiptOrdersRequest struct {
	ReceiptOrderIDs string
	OrderStatus     *int
	Status          *int
}

func (r *ReceiptOrdersRequest) Method() string     { return http.MethodGet }
func (r *ReceiptOrdersRequest) Path() string       { return "/pay/v3/receipt/orders" }
func (r *ReceiptOrdersRequest) Extra() http.Header { return nil }
func (r *ReceiptOrdersRequest) Body() any          { return nil }
func (r *ReceiptOrdersRequest) Query() url.Values {
	q := url.Values{}
	setQ(q, "receiptOrderIds", r.ReceiptOrderIDs)
	if r.OrderStatus != nil {
		q.Set("orderStatus", strconv.Itoa(*r.OrderStatus))
	}
	if r.Status != nil {
		q.Set("status", strconv.Itoa(*r.Status))
	}
	return q
}

// ReceiptAddOrderRequest creates a receipt order.
type ReceiptAddOrderRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	ReceiptOrderID       string
	CurrencyNumber       decimal.Decimal
	CallBackURL          string
}

func (r *ReceiptAddOrderRequest) Method() string     { return http.MethodPost }
func (r *ReceiptAddOrderRequest) Path() string       { return "/pay/v3/receipt/order/add" }
func (r *ReceiptAddOrderRequest) Extra() http.Header { return nil }
func (r *ReceiptAddOrderRequest) Query() url.Values  { return nil }
func (r *ReceiptAddOrderRequest) Body() any {
	return map[string]any{
		"protocol":             r.Protocol,
		"currency":             r.Currency,
		"smartContractAddress": r.SmartContractAddress,
		"companyUserId":        r.CompanyUserID,
		"receiptOrderId":       r.ReceiptOrderID,
		"currencyNumber":       r.CurrencyNumber.String(),
		"callBackUrl":          r.CallBackURL,
	}
}

// ReceiptConfirmRequest confirms a receipt order.
type ReceiptConfirmRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	ReceiptOrderID       string
}

func (r *ReceiptConfirmRequest) Method() string     { return http.MethodPost }
func (r *ReceiptConfirmRequest) Path() string       { return "/pay/v3/receipt/order/confirm" }
func (r *ReceiptConfirmRequest) Extra() http.Header { return nil }
func (r *ReceiptConfirmRequest) Query() url.Values  { return nil }
func (r *ReceiptConfirmRequest) Body() any {
	return map[string]any{
		"protocol":             r.Protocol,
		"currency":             r.Currency,
		"smartContractAddress": r.SmartContractAddress,
		"companyUserId":        r.CompanyUserID,
		"receiptOrderId":       r.ReceiptOrderID,
	}
}
