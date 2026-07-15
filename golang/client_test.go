package novax_test

import (
	"context"
	"io"
	"net/http"
	"strings"
	"sync"
	"testing"

	"github.com/shopspring/decimal"
	novax "novax.dev/sdk"
)

// ── stub transport ────────────────────────────────────────────────────────────

type stubTransport struct {
	mu       sync.Mutex
	captured *http.Request
	body     string
	status   int
}

func (s *stubTransport) RoundTrip(req *http.Request) (*http.Response, error) {
	s.mu.Lock()
	s.captured = req.Clone(req.Context())
	s.mu.Unlock()
	return &http.Response{
		StatusCode: s.status,
		Body:       io.NopCloser(strings.NewReader(s.body)),
		Header:     make(http.Header),
	}, nil
}

func newClient(t *testing.T, stub *stubTransport, opts ...novax.Option) *novax.Client {
	t.Helper()
	base := []novax.Option{
		novax.WithEndpoint("https://api.example.com"),
		novax.WithRoundTripper(stub),
	}
	c, err := novax.NewClient(append(base, opts...)...)
	if err != nil {
		t.Fatalf("NewClient: %v", err)
	}
	return c
}

func stub(body string) *stubTransport {
	return &stubTransport{body: body, status: 200}
}

// ── response parsing ──────────────────────────────────────────────────────────

func TestPayToken_ParsesResult(t *testing.T) {
	s := stub(`{"code":200,"msg":"ok","data":"tok-abc123"}`)
	c := newClient(t, s)

	resp, err := c.Pay.Token(context.Background(), &novax.PayTokenRequest{
		ReceiptOrderID: "order-1",
		CurrencyNumber: decimal.NewFromInt(1),
	})
	if err != nil {
		t.Fatalf("Token: %v", err)
	}
	if !resp.IsSuccess() {
		t.Fatalf("IsSuccess false, code=%v", resp.Code)
	}
	if resp.Data == nil || *resp.Data != "tok-abc123" {
		t.Errorf("Data: got %v, want tok-abc123", resp.Data)
	}
}

func TestPayToken_BusinessError_NoErr(t *testing.T) {
	s := stub(`{"code":400,"msg":"bad request","data":null}`)
	resp, err := newClient(t, s).Pay.Token(context.Background(), &novax.PayTokenRequest{
		ReceiptOrderID: "x",
		CurrencyNumber: decimal.NewFromInt(1),
	})
	if err != nil {
		t.Fatalf("unexpected error: %v", err)
	}
	if resp.IsSuccess() {
		t.Error("expected IsSuccess=false for code 400")
	}
}

// ── request construction ──────────────────────────────────────────────────────

func TestPayToken_GET_URLAndQuery(t *testing.T) {
	s := stub(`{"code":200,"data":"t"}`)
	newClient(t, s).Pay.Token(context.Background(), &novax.PayTokenRequest{ //nolint:errcheck
		ReceiptOrderID: "order-99",
		CurrencyNumber: decimal.NewFromFloat(1.5),
	})

	req := s.captured
	if req.Method != http.MethodGet {
		t.Errorf("method: got %s, want GET", req.Method)
	}
	if !strings.Contains(req.URL.RawQuery, "receiptOrderId=order-99") {
		t.Errorf("query missing receiptOrderId: %s", req.URL.RawQuery)
	}
	if !strings.Contains(req.URL.RawQuery, "currencyNumber=1.5") {
		t.Errorf("query missing currencyNumber: %s", req.URL.RawQuery)
	}
}

func TestReceiptAddOrder_POST_ContentType(t *testing.T) {
	s := stub(`{"code":200,"data":null}`)
	newClient(t, s).Pay.ReceiptAddOrder(context.Background(), &novax.ReceiptAddOrderRequest{ //nolint:errcheck
		Protocol: "TRC20", Currency: "usdt",
		SmartContractAddress: "Taddr", CompanyUserID: "88888896",
		ReceiptOrderID: "r-1", CurrencyNumber: decimal.NewFromInt(1),
	})

	req := s.captured
	if req.Method != http.MethodPost {
		t.Errorf("method: got %s, want POST", req.Method)
	}
	if ct := req.Header.Get("Content-Type"); ct != "application/json" {
		t.Errorf("Content-Type: got %q, want application/json", ct)
	}
}

// ── header injection ──────────────────────────────────────────────────────────

func TestIPAndLanguageHeaders(t *testing.T) {
	s := stub(`{"code":200,"data":"t"}`)
	c := newClient(t, s, novax.WithClientIP("1.2.3.4"), novax.WithLanguage("zh-CN"))

	c.Pay.Token(context.Background(), &novax.PayTokenRequest{ //nolint:errcheck
		ReceiptOrderID: "o", CurrencyNumber: decimal.NewFromInt(1),
	})

	if got := s.captured.Header.Get("ip"); got != "1.2.3.4" {
		t.Errorf("ip: got %q, want 1.2.3.4", got)
	}
	if got := s.captured.Header.Get("language"); got != "zh-CN" {
		t.Errorf("language: got %q, want zh-CN", got)
	}
}

func TestNoUserIDHeader(t *testing.T) {
	// userId is injected server-side from X-Access-Key — the SDK must never send it.
	s := stub(`{"code":200,"data":"t"}`)
	newClient(t, s).Pay.Token(context.Background(), &novax.PayTokenRequest{ //nolint:errcheck
		ReceiptOrderID: "o", CurrencyNumber: decimal.NewFromInt(1),
	})
	if s.captured.Header.Get("userId") != "" {
		t.Error("userId must not be sent — it is injected server-side")
	}
}

// ── signature ─────────────────────────────────────────────────────────────────

func TestSignedPath_SetsSignatureHeaders(t *testing.T) {
	s := stub(`{"code":200,"data":"t"}`)
	c := newClient(t, s, novax.WithAccessKey("ak-test", "sk-test"))

	c.Pay.Token(context.Background(), &novax.PayTokenRequest{ //nolint:errcheck
		ReceiptOrderID: "o", CurrencyNumber: decimal.NewFromInt(1),
	})

	if got := s.captured.Header.Get("X-Access-Key"); got != "ak-test" {
		t.Errorf("X-Access-Key: got %q, want ak-test", got)
	}
	if sig := s.captured.Header.Get("X-Signature"); len(sig) != 64 {
		t.Errorf("X-Signature should be 64-char hex, got len=%d", len(sig))
	}
	if ts := s.captured.Header.Get("X-Timestamp"); ts == "" {
		t.Error("X-Timestamp must not be empty")
	}
}

func TestPublicPath_NoSignatureHeaders(t *testing.T) {
	s := stub(`{"code":200,"data":null}`)
	c := newClient(t, s, novax.WithAccessKey("ak-test", "sk-test"))

	// /pay/public/h5/order does NOT match /api/pay/v3/*
	c.Pay.H5Order(context.Background(), &novax.H5OrderRequest{Token: "tok"}) //nolint:errcheck

	if s.captured.Header.Get("X-Access-Key") != "" {
		t.Error("public path must not have X-Access-Key")
	}
}

// ── request types ─────────────────────────────────────────────────────────────

func TestPayTokenRequest_Fields(t *testing.T) {
	req := &novax.PayTokenRequest{ReceiptOrderID: "r1", CurrencyNumber: decimal.NewFromFloat(1.5)}
	if req.Method() != http.MethodGet {
		t.Errorf("Method: want GET, got %s", req.Method())
	}
	if req.Path() != "/pay/v3/token" {
		t.Errorf("Path: want /pay/v3/token, got %s", req.Path())
	}
	if req.Body() != nil {
		t.Error("Body: want nil")
	}
	q := req.Query()
	if q.Get("receiptOrderId") != "r1" {
		t.Errorf("receiptOrderId: got %q", q.Get("receiptOrderId"))
	}
	if q.Get("currencyNumber") != "1.5" {
		t.Errorf("currencyNumber: got %q", q.Get("currencyNumber"))
	}
}

func TestReceiptProtocolsRequest_DefaultType(t *testing.T) {
	req := &novax.ReceiptProtocolsRequest{} // zero value → default type=1
	if got := req.Query().Get("type"); got != "1" {
		t.Errorf("type: got %q, want 1", got)
	}
}

func TestH5OrderRequest_EmptyToken_NoQueryParam(t *testing.T) {
	req := &novax.H5OrderRequest{}
	if got := req.Query().Get("token"); got != "" {
		t.Errorf("expected no token param, got %q", got)
	}
}
