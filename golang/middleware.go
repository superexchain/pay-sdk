package novax

import (
	"bytes"
	"io"
	"log/slog"
	"net/http"
	"strconv"
	"time"
)

// Middleware wraps an http.RoundTripper to add cross-cutting behaviour.
// This is the idiomatic Go HTTP middleware pattern.
type Middleware func(http.RoundTripper) http.RoundTripper

type roundTripperFunc func(*http.Request) (*http.Response, error)

func (f roundTripperFunc) RoundTrip(r *http.Request) (*http.Response, error) { return f(r) }

// HeaderMiddleware appends fixed headers to every request.
func HeaderMiddleware(headers map[string]string) Middleware {
	return func(next http.RoundTripper) http.RoundTripper {
		return roundTripperFunc(func(req *http.Request) (*http.Response, error) {
			req = req.Clone(req.Context())
			for k, v := range headers {
				req.Header.Add(k, v)
			}
			return next.RoundTrip(req)
		})
	}
}

// SignatureMiddleware stamps X-Access-Key / X-Signature / X-Timestamp on
// signed paths. Only /api/pay/v3/* is signed; /pay/public/* passes through.
func SignatureMiddleware(creds *AccessKeyCredentials) Middleware {
	return func(next http.RoundTripper) http.RoundTripper {
		return roundTripperFunc(func(req *http.Request) (*http.Response, error) {
			if !shouldSign(req.URL.Path) {
				return next.RoundTrip(req)
			}
			req = req.Clone(req.Context())

			var body []byte
			if req.Body != nil {
				var err error
				body, err = io.ReadAll(req.Body)
				if err != nil {
					return nil, err
				}
				req.Body = io.NopCloser(bytes.NewReader(body))
				req.GetBody = func() (io.ReadCloser, error) {
					return io.NopCloser(bytes.NewReader(body)), nil
				}
			}

			ts := time.Now().UnixMilli()
			data := dataToSign(req.Method, req.URL.String(), body, ts)
			sig := hmacSHA256Hex(data, creds.AccessSecret)

			req.Header.Set("X-Access-Key", creds.AccessKey)
			req.Header.Set("X-Signature", sig)
			req.Header.Set("X-Timestamp", strconv.FormatInt(ts, 10))
			return next.RoundTrip(req)
		})
	}
}

// LoggingMiddleware logs outbound requests and inbound responses via slog.
// HTTP ≥500 is logged at Error with the response body (truncated to 2 KB).
func LoggingMiddleware() Middleware {
	return func(next http.RoundTripper) http.RoundTripper {
		return roundTripperFunc(func(req *http.Request) (*http.Response, error) {
			slog.Info("->", "method", req.Method, "url", req.URL.String())
			t0 := time.Now()
			resp, err := next.RoundTrip(req)
			if err != nil {
				slog.Error("request error", "err", err)
				return nil, err
			}
			elapsed := time.Since(t0)
			if resp.StatusCode >= 500 {
				body, _ := io.ReadAll(resp.Body)
				resp.Body = io.NopCloser(bytes.NewReader(body))
				preview := string(body)
				if len(preview) > 2048 {
					preview = preview[:2048] + "…"
				}
				slog.Error("<-", "status", resp.StatusCode, "elapsed", elapsed, "body", preview)
			} else {
				slog.Info("<-", "status", resp.StatusCode, "elapsed", elapsed)
			}
			return resp, nil
		})
	}
}
