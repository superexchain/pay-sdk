package novax

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"net/url"
	"sort"
	"strconv"
	"strings"
)

func dataToSign(method, rawURL string, body []byte, tsMs int64) string {
	parts := []string{strings.ToUpper(method)}
	if q := sortedQuery(rawURL); q != "" {
		parts = append(parts, q)
	}
	if len(body) > 0 {
		if s := sortedBody(body); s != "" {
			parts = append(parts, s)
		}
	}
	parts = append(parts, fmt.Sprintf("timestamp=%d", tsMs))
	return strings.Join(parts, "&")
}

func hmacSHA256Hex(data, secret string) string {
	mac := hmac.New(sha256.New, []byte(secret))
	mac.Write([]byte(data))
	return hex.EncodeToString(mac.Sum(nil))
}

func shouldSign(path string) bool {
	return strings.HasPrefix(path, "/api/pay/v3/")
}

func sortedQuery(rawURL string) string {
	idx := strings.Index(rawURL, "?")
	if idx < 0 {
		return ""
	}
	grouped := make(map[string][]string)
	var keys []string
	for _, pair := range strings.Split(rawURL[idx+1:], "&") {
		if pair == "" {
			continue
		}
		k, v, _ := strings.Cut(pair, "=")
		dk, _ := url.QueryUnescape(k)
		dv, _ := url.QueryUnescape(v)
		if _, exists := grouped[dk]; !exists {
			keys = append(keys, dk)
		}
		grouped[dk] = append(grouped[dk], dv)
	}
	sort.Strings(keys)
	parts := make([]string, 0, len(keys))
	for _, k := range keys {
		parts = append(parts, k+"="+strings.Join(grouped[k], ","))
	}
	return strings.Join(parts, "&")
}

func sortedBody(body []byte) string {
	var v any
	if err := json.Unmarshal(body, &v); err != nil {
		return ""
	}
	switch val := v.(type) {
	case []any:
		parts := make([]string, 0, len(val))
		for _, item := range val {
			parts = append(parts, sortObject(item))
		}
		return strings.Join(parts, "&")
	default:
		return sortObject(v)
	}
}

func sortObject(v any) string {
	m, ok := v.(map[string]any)
	if !ok {
		return fmt.Sprintf("%v", v)
	}
	keys := make([]string, 0, len(m))
	for k := range m {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	parts := make([]string, 0, len(keys))
	for _, k := range keys {
		parts = append(parts, k+"="+renderValue(m[k]))
	}
	return strings.Join(parts, "&")
}

func renderValue(v any) string {
	switch val := v.(type) {
	case nil:
		return ""
	case string:
		return val
	case bool:
		if val {
			return "true"
		}
		return "false"
	case float64:
		// Use integer representation when the value has no fractional part,
		// matching the server-side getSortedBodyString behaviour.
		if val == float64(int64(val)) {
			return fmt.Sprintf("%d", int64(val))
		}
		return strconv.FormatFloat(val, 'f', -1, 64)
	case map[string]any, []any:
		b, _ := json.Marshal(val)
		return string(b)
	default:
		return fmt.Sprintf("%v", val)
	}
}
