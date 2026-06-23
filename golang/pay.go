package novax

import (
	"context"
	"encoding/json"
)

// PayClient provides typed methods for all pay API endpoints.
// Access it via Client.Pay — never construct directly.
type PayClient struct {
	c *Client
}

// ── H5 ────────────────────────────────────────────────────────────────────────

// Token fetches an H5 payment token. GET /pay/v3/token
func (p *PayClient) Token(ctx context.Context, req *PayTokenRequest) (*ReturnResult[string], error) {
	return execute[string](ctx, p.c, req)
}

// H5Order returns the H5 payment order summary. GET /pay/public/h5/order
func (p *PayClient) H5Order(ctx context.Context, req *H5OrderRequest) (*ReturnResult[PayOrderResp], error) {
	return execute[PayOrderResp](ctx, p.c, req)
}

// H5Protocols lists protocols available for an H5 payment. GET /pay/public/h5/protocols
func (p *PayClient) H5Protocols(ctx context.Context, req *H5ProtocolsRequest) (*ReturnResult[[]PayProtocolsResp], error) {
	return execute[[]PayProtocolsResp](ctx, p.c, req)
}

// H5Address returns the H5 payment receipt address. GET /pay/public/h5/address
func (p *PayClient) H5Address(ctx context.Context, req *H5AddressRequest) (*ReturnResult[PayOrderAddressResp], error) {
	return execute[PayOrderAddressResp](ctx, p.c, req)
}

// H5OkTime confirms the H5 payment time. POST /pay/public/h5/ok-time
func (p *PayClient) H5OkTime(ctx context.Context, req *H5OkTimeRequest) (*ReturnResult[bool], error) {
	return execute[bool](ctx, p.c, req)
}

// H5Confirm confirms the H5 payment. POST /pay/public/h5/confirm
func (p *PayClient) H5Confirm(ctx context.Context, req *H5ConfirmRequest) (*ReturnResult[json.RawMessage], error) {
	return execute[json.RawMessage](ctx, p.c, req)
}

// H5OrderStatus polls the H5 order pay status. GET /pay/public/h5/order/pay/status
func (p *PayClient) H5OrderStatus(ctx context.Context, req *H5OrderStatusRequest) (*ReturnResult[PayOrderAddressResp], error) {
	return execute[PayOrderAddressResp](ctx, p.c, req)
}

// ── QRCode ────────────────────────────────────────────────────────────────────

// DynamicQrPayCreate creates a dynamic QR-code pay order. POST /pay/v3/qr-code-pay/dynamic/create
func (p *PayClient) DynamicQrPayCreate(ctx context.Context, req *DynamicQrPayCreateRequest) (*ReturnResult[string], error) {
	return execute[string](ctx, p.c, req)
}

// ── Receipt ───────────────────────────────────────────────────────────────────

// ReceiptProtocols lists supported chains. GET /pay/v3/protocols
func (p *PayClient) ReceiptProtocols(ctx context.Context, req *ReceiptProtocolsRequest) (*ReturnResult[[]PayProtocolsResp], error) {
	return execute[[]PayProtocolsResp](ctx, p.c, req)
}

// ReceiptAddress looks up a fixed receipt address. GET /pay/v3/receipt/address
func (p *PayClient) ReceiptAddress(ctx context.Context, req *ReceiptAddressRequest) (*ReturnResult[PayOrderAddressFixedResp], error) {
	return execute[PayOrderAddressFixedResp](ctx, p.c, req)
}

// ReceiptOrders queries receipt orders. GET /pay/v3/receipt/orders
func (p *PayClient) ReceiptOrders(ctx context.Context, req *ReceiptOrdersRequest) (*ReturnResult[[]ReceiptOrderResp], error) {
	return execute[[]ReceiptOrderResp](ctx, p.c, req)
}

// ReceiptAddOrder creates a receipt order. POST /pay/v3/receipt/order/add
func (p *PayClient) ReceiptAddOrder(ctx context.Context, req *ReceiptAddOrderRequest) (*ReturnResult[ReceiptOrderAddressResp], error) {
	return execute[ReceiptOrderAddressResp](ctx, p.c, req)
}

// ReceiptConfirm confirms a receipt order. POST /pay/v3/receipt/order/confirm
func (p *PayClient) ReceiptConfirm(ctx context.Context, req *ReceiptConfirmRequest) (*ReturnResult[json.RawMessage], error) {
	return execute[json.RawMessage](ctx, p.c, req)
}

// ── Withdraw ──────────────────────────────────────────────────────────────────

// WithdrawOrders queries withdraw orders. GET /pay/v3/withdraw/orders
func (p *PayClient) WithdrawOrders(ctx context.Context, req *WithdrawOrdersRequest) (*ReturnResult[[]WithdrawOrderResp], error) {
	return execute[[]WithdrawOrderResp](ctx, p.c, req)
}

// WithdrawAddOrder creates a withdraw order. POST /pay/v3/withdraw/order/add
func (p *PayClient) WithdrawAddOrder(ctx context.Context, req *WithdrawAddOrderRequest) (*ReturnResult[WithdrawOrderResp], error) {
	return execute[WithdrawOrderResp](ctx, p.c, req)
}
