package novax

import (
	"net/http"
	"net/url"
	"strconv"

	"github.com/shopspring/decimal"
)

// WithdrawOrdersRequest queries withdraw orders.
type WithdrawOrdersRequest struct {
	WithdrawOrderIDs string
	Status           *int
}

func (r *WithdrawOrdersRequest) Method() string     { return http.MethodGet }
func (r *WithdrawOrdersRequest) Path() string       { return "/pay/v3/withdraw/orders" }
func (r *WithdrawOrdersRequest) Extra() http.Header { return nil }
func (r *WithdrawOrdersRequest) Body() any          { return nil }
func (r *WithdrawOrdersRequest) Query() url.Values {
	q := url.Values{}
	setQ(q, "withdrawOrderIds", r.WithdrawOrderIDs)
	if r.Status != nil {
		q.Set("status", strconv.Itoa(*r.Status))
	}
	return q
}

// WithdrawAddOrderRequest creates a withdraw order.
type WithdrawAddOrderRequest struct {
	WithdrawOrderID      string
	OrderType            int
	Currency             string
	Protocol             string
	SmartContractAddress string
	Address              string
	CurrencyNumber       decimal.Decimal
	CallBackURL          string
}

func (r *WithdrawAddOrderRequest) Method() string     { return http.MethodPost }
func (r *WithdrawAddOrderRequest) Path() string       { return "/pay/v3/withdraw/order/add" }
func (r *WithdrawAddOrderRequest) Extra() http.Header { return nil }
func (r *WithdrawAddOrderRequest) Query() url.Values  { return nil }
func (r *WithdrawAddOrderRequest) Body() any {
	return map[string]any{
		"withdrawOrderId":      r.WithdrawOrderID,
		"type":                 r.OrderType,
		"currency":             r.Currency,
		"protocol":             r.Protocol,
		"smartContractAddress": r.SmartContractAddress,
		"address":              r.Address,
		"currencyNumber":       r.CurrencyNumber.String(),
		"callBackUrl":          r.CallBackURL,
	}
}
