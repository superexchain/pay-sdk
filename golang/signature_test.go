package novax

import "testing"

func TestSortedQuery(t *testing.T) {
	got := dataToSign("GET", "https://api.novax.com/pay/v3/token?b=2&a=1&a=11", nil, 1700000000000)
	if want := "GET&a=1,11&b=2&timestamp=1700000000000"; got != want {
		t.Errorf("got %q\nwant %q", got, want)
	}
}

func TestSortedBody(t *testing.T) {
	body := []byte(`{"chain":"TRC20","amount":100,"address":"Txxx"}`)
	got := dataToSign("POST", "https://api.novax.com/pay/v3/withdraw/order/add", body, 1700000000000)
	if want := "POST&address=Txxx&amount=100&chain=TRC20&timestamp=1700000000000"; got != want {
		t.Errorf("got %q\nwant %q", got, want)
	}
}

func TestHMACSHA256(t *testing.T) {
	got := hmacSHA256Hex("hello", "secret")
	want := "88aab3ede8d3adf94d26ab90d3bafd4a2083070c3bcce9c014ee04a443847c0b"
	if got != want {
		t.Errorf("got %q\nwant %q", got, want)
	}
}
