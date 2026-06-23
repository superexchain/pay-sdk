package novax

import (
	"net/http"
	"net/url"
)

// Request describes a single API endpoint's wire requirements.
// All concrete request types implement this interface;
// advanced users may implement it directly for custom endpoints.
type Request interface {
	Method() string
	Path() string
	Query() url.Values
	Extra() http.Header
	Body() any
}
