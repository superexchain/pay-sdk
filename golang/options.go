package novax

import (
	"net/http"
	"time"
)

// Option configures a Client.
type Option func(*config)

type config struct {
	endpoint    string
	creds       *AccessKeyCredentials
	timeout     time.Duration
	headers     map[string]string
	middlewares []Middleware
	insecureTLS bool
	baseRT      http.RoundTripper // override base transport (for testing)
}

// WithEndpoint sets the base URL (required).
func WithEndpoint(endpoint string) Option {
	return func(c *config) { c.endpoint = endpoint }
}

// WithAccessKey sets the access key and secret used for HMAC signing.
// user_id is resolved server-side from the access key — never supply it.
func WithAccessKey(key, secret string) Option {
	return func(c *config) {
		c.creds = &AccessKeyCredentials{AccessKey: key, AccessSecret: secret}
	}
}

// WithClientIP sets the ip header — used for server-side IP whitelist checks.
func WithClientIP(ip string) Option {
	return func(c *config) { c.header("ip", ip) }
}

// WithLanguage sets the language header.
func WithLanguage(lang string) Option {
	return func(c *config) { c.header("language", lang) }
}

// WithTimeout overrides the default 30s request timeout.
func WithTimeout(d time.Duration) Option {
	return func(c *config) { c.timeout = d }
}

// WithMiddleware adds custom middleware (e.g. LoggingMiddleware()).
// Middleware executes outermost first: the first option wraps outermost.
func WithMiddleware(m Middleware) Option {
	return func(c *config) { c.middlewares = append(c.middlewares, m) }
}

// WithInsecureTLS disables TLS certificate validation. DEV ONLY.
func WithInsecureTLS() Option {
	return func(c *config) { c.insecureTLS = true }
}

// WithRoundTripper replaces the base http.RoundTripper. For testing only.
func WithRoundTripper(rt http.RoundTripper) Option {
	return func(c *config) { c.baseRT = rt }
}

func (c *config) header(k, v string) {
	if c.headers == nil {
		c.headers = make(map[string]string)
	}
	c.headers[k] = v
}
