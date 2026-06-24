package novax

import (
	"bytes"
	"context"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io"
	"log/slog"
	"net/http"
	"strings"
	"time"
)

// Client is the entrypoint for all API calls.
// Create one with NewClient; it is safe for concurrent use.
//
//	client, err := novax.NewClient(
//	    novax.WithEndpoint("https://api.novax.dev/api"),
//	    novax.WithAccessKey("key", "secret"),
//	)
//	resp, err := client.Pay.Token(ctx, &novax.PayTokenRequest{...})
type Client struct {
	Pay      *PayClient
	endpoint string
	hc       *http.Client
}

// NewClient creates a Client with the provided options.
func NewClient(opts ...Option) (*Client, error) {
	cfg := &config{timeout: 30 * time.Second}
	for _, o := range opts {
		o(cfg)
	}
	if cfg.endpoint == "" {
		return nil, fmt.Errorf("novax: endpoint is required")
	}

	var base http.RoundTripper
	if cfg.baseRT != nil {
		base = cfg.baseRT
	} else {
		base = &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: cfg.insecureTLS}, //nolint:gosec
		}
	}

	// Build middleware stack. Applied innermost→outermost:
	// transport → signature → header → user middlewares
	tr := base
	if cfg.creds != nil {
		tr = SignatureMiddleware(cfg.creds)(tr)
	}
	if len(cfg.headers) > 0 {
		tr = HeaderMiddleware(cfg.headers)(tr)
	}
	for i := len(cfg.middlewares) - 1; i >= 0; i-- {
		tr = cfg.middlewares[i](tr)
	}

	c := &Client{
		endpoint: strings.TrimRight(cfg.endpoint, "/"),
		hc:       &http.Client{Timeout: cfg.timeout, Transport: tr},
	}
	c.Pay = &PayClient{c: c}
	return c, nil
}

// execute is the internal generic execution function.
// Primary API is through typed methods on PayClient (client.Pay.Token, etc.).
func execute[T any](ctx context.Context, c *Client, req Request) (*ReturnResult[T], error) {
	httpReq, err := buildHTTPRequest(ctx, c.endpoint, req)
	if err != nil {
		return nil, err
	}
	resp, err := c.hc.Do(httpReq)
	if err != nil {
		return nil, &TransportError{Cause: err}
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, &TransportError{Cause: err}
	}

	var result ReturnResult[T]
	if err := json.Unmarshal(body, &result); err != nil {
		return nil, &JSONError{Cause: err}
	}
	if !result.IsSuccess() {
		var code any
		if result.Code != nil {
			code = *result.Code
		}
		var msg any
		if result.Msg != nil {
			msg = *result.Msg
		}
		slog.Warn("business error",
			"method", req.Method(), "path", req.Path(),
			"code", code, "msg", msg,
		)
	}
	return &result, nil
}

func buildHTTPRequest(ctx context.Context, endpoint string, req Request) (*http.Request, error) {
	rawURL := buildURL(endpoint, req.Path(), req.Query())
	var body io.Reader
	if b := req.Body(); b != nil {
		data, err := json.Marshal(b)
		if err != nil {
			return nil, &JSONError{Cause: err}
		}
		body = bytes.NewReader(data)
	}
	r, err := http.NewRequestWithContext(ctx, req.Method(), rawURL, body)
	if err != nil {
		return nil, fmt.Errorf("novax: %w", err)
	}
	if req.Body() != nil {
		r.Header.Set("Content-Type", "application/json")
	}
	for k, vs := range req.Extra() {
		for _, v := range vs {
			r.Header.Add(k, v)
		}
	}
	return r, nil
}

func buildURL(endpoint, path string, params interface{ Encode() string }) string {
	if !strings.HasPrefix(path, "/") {
		path = "/" + path
	}
	u := endpoint + path
	if params != nil {
		if q := params.Encode(); q != "" {
			u += "?" + q
		}
	}
	return u
}
