package novax

import (
	"net/http"
	"net/url"

	"github.com/shopspring/decimal"
)

// DynamicQrPayCreateRequest creates a dynamic QR-code pay order.
type DynamicQrPayCreateRequest struct {
	ReceiptOrderID string
	CompanyUserID  int64
	Currency       string
	Amount         decimal.Decimal
	Comment        string
}

func (r *DynamicQrPayCreateRequest) Method() string     { return http.MethodPost }
func (r *DynamicQrPayCreateRequest) Path() string       { return "/pay/v3/qr-code-pay/dynamic/create" }
func (r *DynamicQrPayCreateRequest) Extra() http.Header { return nil }
func (r *DynamicQrPayCreateRequest) Query() url.Values  { return nil }
func (r *DynamicQrPayCreateRequest) Body() any {
	return map[string]any{
		"receiptOrderId": r.ReceiptOrderID,
		"companyUserId":  r.CompanyUserID,
		"currency":       r.Currency,
		"amount":         r.Amount.String(),
		"comment":        r.Comment,
	}
}
