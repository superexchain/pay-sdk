package novax_test

import (
	"context"
	"fmt"
	"sync"
	"testing"
	"time"

	"github.com/shopspring/decimal"
	novax "novax.dev/sdk"
)

const (
	devAccessKey    = "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD"
	devAccessSecret = "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2"
)

var (
	devOnce      sync.Once
	devClientVal *novax.Client
)

func devClient() *novax.Client {
	devOnce.Do(func() {
		var err error
		devClientVal, err = novax.NewClient(
			novax.WithEndpoint("https://api.novax.dev/api"),
			novax.WithAccessKey(devAccessKey, devAccessSecret),
			novax.WithClientIP("1.2.3.4"),
			novax.WithLanguage("zh-CN"),
			novax.WithMiddleware(novax.LoggingMiddleware()),
			novax.WithInsecureTLS(),
		)
		if err != nil {
			panic(err)
		}
	})
	return devClientVal
}

func devToken(t *testing.T) string {
	t.Helper()
	resp, err := devClient().Pay.Token(context.Background(), &novax.PayTokenRequest{
		ReceiptOrderID: "1234",
		CurrencyNumber: decimal.NewFromInt(1),
	})
	if err != nil || resp == nil || !resp.IsSuccess() || resp.Data == nil {
		t.Skipf("skip: token unavailable: err=%v", err)
	}
	return *resp.Data
}

func tsStr() string { return fmt.Sprintf("%d", time.Now().UnixMilli()) }

func TestIntegration_PayToken(t *testing.T) {
	token := devToken(t)
	t.Logf("token = %s", token)
}

func TestIntegration_H5Order(t *testing.T) {
	resp, err := devClient().Pay.H5Order(context.Background(), &novax.H5OrderRequest{Token: devToken(t)})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_H5Protocols(t *testing.T) {
	resp, err := devClient().Pay.H5Protocols(context.Background(), &novax.H5ProtocolsRequest{Token: devToken(t)})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_ReceiptProtocols(t *testing.T) {
	resp, err := devClient().Pay.ReceiptProtocols(context.Background(), &novax.ReceiptProtocolsRequest{ReceiptType: 1})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_ReceiptOrders(t *testing.T) {
	resp, err := devClient().Pay.ReceiptOrders(context.Background(), &novax.ReceiptOrdersRequest{})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_ReceiptAddOrder(t *testing.T) {
	resp, err := devClient().Pay.ReceiptAddOrder(context.Background(), &novax.ReceiptAddOrderRequest{
		Protocol:             "TRC20",
		Currency:             "USDT",
		SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
		CompanyUserID:        "88888896",
		ReceiptOrderID:       tsStr(),
		CurrencyNumber:       decimal.NewFromInt(1),
		CallBackURL:          "http://tripartite-payment-ts.dev.svc.cluster.local/v3/public/test/call/back",
	})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_WithdrawOrders(t *testing.T) {
	resp, err := devClient().Pay.WithdrawOrders(context.Background(), &novax.WithdrawOrdersRequest{
		WithdrawOrderIDs: "w_1234,1745564918818000",
	})
	if err != nil {
		t.Fatal(err)
	}
	t.Logf("%+v", resp)
}

func TestIntegration_DynamicQrPayCreate(t *testing.T) {
	resp, err := devClient().Pay.DynamicQrPayCreate(context.Background(), &novax.DynamicQrPayCreateRequest{
		ReceiptOrderID: tsStr(),
		CompanyUserID:  88_888_896,
		Currency:       "usdt",
		Amount:         decimal.NewFromInt(1),
		Comment:        "test",
	})
	if err != nil {
		t.Fatal(err)
	}
	if resp.Msg == nil || *resp.Msg == "" {
		t.Error("expected non-empty Msg (server returns order id in Msg)")
	}
	t.Logf("%+v", resp)
}
