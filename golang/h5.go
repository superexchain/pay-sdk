package novax

import (
	"net/http"
	"net/url"

	"github.com/shopspring/decimal"
)

// PayTokenRequest fetches an H5 payment token.
// userId is resolved server-side from X-Access-Key; callers never supply it.
type PayTokenRequest struct {
	ReceiptOrderID string
	CurrencyNumber decimal.Decimal
}

func (r *PayTokenRequest) Method() string     { return http.MethodGet }
func (r *PayTokenRequest) Path() string       { return "/pay/v3/token" }
func (r *PayTokenRequest) Extra() http.Header { return nil }
func (r *PayTokenRequest) Body() any          { return nil }
func (r *PayTokenRequest) Query() url.Values {
	return url.Values{
		"receiptOrderId": {r.ReceiptOrderID},
		"currencyNumber": {r.CurrencyNumber.String()},
	}
}

// H5OrderRequest fetches the H5 payment order summary.
type H5OrderRequest struct{ Token string }

func (r *H5OrderRequest) Method() string     { return http.MethodGet }
func (r *H5OrderRequest) Path() string       { return "/pay/public/h5/order" }
func (r *H5OrderRequest) Extra() http.Header { return nil }
func (r *H5OrderRequest) Body() any          { return nil }
func (r *H5OrderRequest) Query() url.Values  { return optionalQuery("token", r.Token) }

// H5ProtocolsRequest lists protocols available for an H5 payment.
type H5ProtocolsRequest struct{ Token string }

func (r *H5ProtocolsRequest) Method() string     { return http.MethodGet }
func (r *H5ProtocolsRequest) Path() string       { return "/pay/public/h5/protocols" }
func (r *H5ProtocolsRequest) Extra() http.Header { return nil }
func (r *H5ProtocolsRequest) Body() any          { return nil }
func (r *H5ProtocolsRequest) Query() url.Values  { return optionalQuery("token", r.Token) }

// H5AddressRequest gets the H5 payment receipt address.
type H5AddressRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	Token                string
}

func (r *H5AddressRequest) Method() string     { return http.MethodGet }
func (r *H5AddressRequest) Path() string       { return "/pay/public/h5/address" }
func (r *H5AddressRequest) Extra() http.Header { return nil }
func (r *H5AddressRequest) Body() any          { return nil }
func (r *H5AddressRequest) Query() url.Values {
	q := url.Values{}
	setQ(q, "protocol", r.Protocol)
	setQ(q, "currency", r.Currency)
	setQ(q, "smartContractAddress", r.SmartContractAddress)
	setQ(q, "companyUserId", r.CompanyUserID)
	setQ(q, "token", r.Token)
	return q
}

// H5OkTimeRequest confirms the H5 payment time.
type H5OkTimeRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	Token                string
}

func (r *H5OkTimeRequest) Method() string     { return http.MethodPost }
func (r *H5OkTimeRequest) Path() string       { return "/pay/public/h5/ok-time" }
func (r *H5OkTimeRequest) Extra() http.Header { return nil }
func (r *H5OkTimeRequest) Query() url.Values  { return nil }
func (r *H5OkTimeRequest) Body() any {
	return map[string]any{
		"protocol": r.Protocol, "currency": r.Currency,
		"smartContractAddress": r.SmartContractAddress,
		"companyUserId":        r.CompanyUserID, "token": r.Token,
	}
}

// H5ConfirmRequest confirms the H5 payment.
type H5ConfirmRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	Token                string
}

func (r *H5ConfirmRequest) Method() string     { return http.MethodPost }
func (r *H5ConfirmRequest) Path() string       { return "/pay/public/h5/confirm" }
func (r *H5ConfirmRequest) Extra() http.Header { return nil }
func (r *H5ConfirmRequest) Query() url.Values  { return nil }
func (r *H5ConfirmRequest) Body() any {
	return map[string]any{
		"protocol": r.Protocol, "currency": r.Currency,
		"smartContractAddress": r.SmartContractAddress,
		"companyUserId":        r.CompanyUserID, "token": r.Token,
	}
}

// H5OrderStatusRequest polls the H5 order pay status.
type H5OrderStatusRequest struct {
	Protocol             string
	Currency             string
	SmartContractAddress string
	CompanyUserID        string
	Token                string
}

func (r *H5OrderStatusRequest) Method() string     { return http.MethodGet }
func (r *H5OrderStatusRequest) Path() string       { return "/pay/public/h5/order/pay/status" }
func (r *H5OrderStatusRequest) Extra() http.Header { return nil }
func (r *H5OrderStatusRequest) Body() any          { return nil }
func (r *H5OrderStatusRequest) Query() url.Values {
	q := url.Values{}
	setQ(q, "protocol", r.Protocol)
	setQ(q, "currency", r.Currency)
	setQ(q, "smartContractAddress", r.SmartContractAddress)
	setQ(q, "companyUserId", r.CompanyUserID)
	setQ(q, "token", r.Token)
	return q
}

// ── helpers ───────────────────────────────────────────────────────────────────

func optionalQuery(key, val string) url.Values {
	q := url.Values{}
	setQ(q, key, val)
	return q
}

func setQ(q url.Values, key, val string) {
	if val != "" {
		q.Set(key, val)
	}
}
