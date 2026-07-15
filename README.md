# Novax SDK

Multi-language SDK for the Novax Pay API. Supports Java, Python, Go, Rust, and C#.

All SDKs share the same design: a single client entry point, request objects per endpoint, and a unified `ReturnResult<T>` response envelope.

## Languages

| Language | Min Version | Package |
|---|---|---|
| [Java](#java) | Java 17 | Maven (`com.novax:sdk-pay`) |
| [Python](#python) | Python 3.11 | pip (`novax-sdk`) |
| [Go](#go) | Go 1.21 | `novax.dev/sdk` |
| [Rust](#rust) | Rust 2021 | Cargo (`novax-sdk`) |
| [C#](#c) | .NET 8 | NuGet (`NovaxSdk`) |

---

## Response Envelope

Every API call returns `ReturnResult<T>`:

| Field | Type | Description |
|---|---|---|
| `code` | int | `200` = success |
| `msg` | string | Human-readable message |
| `data` | T | Typed payload (null on error) |

---

## Java

**Requirements:** Java 17, Maven

### Dependencies (`pom.xml`)

```xml
<dependency>
    <groupId>com.novax</groupId>
    <artifactId>sdk-pay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Initialize client

```java
import com.novax.sdk.core.NovaxClient;

NovaxClient client = NovaxClient.builder()
    .endpoint("https://api.novax.dev/api")
    .accessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .clientIp("1.2.3.4")      // optional: IP whitelist header
    .language("zh-CN")         // optional: language header
    .build();
```

### H5 Payment — get token

```java
import com.novax.sdk.pay.h5.PayTokenRequest;
import com.novax.sdk.core.model.ReturnResult;
import java.math.BigDecimal;

ReturnResult<String> result = client.execute(
    new PayTokenRequest("receipt-order-001", new BigDecimal("100.00"))
);

if (result.isSuccess()) {
    String token = result.data();
}
```

### Receipt — create order

```java
import com.novax.sdk.pay.receipt.ReceiptAddOrderRequest;
import com.novax.sdk.pay.receipt.ReceiptOrderAddressResp;

ReturnResult<ReceiptOrderAddressResp> result = client.execute(
    ReceiptAddOrderRequest.builder()
        .protocol("TRC20")
        .currency("usdt")
        .smartContractAddress("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")
        .companyUserId("user-123")
        .receiptOrderId("order-001")
        .currencyNumber(new BigDecimal("50.00"))
        .callBackUrl("https://your.domain/callback")
        .build()
);
```

### Withdraw — create order

```java
import com.novax.sdk.pay.withdraw.WithdrawAddOrderRequest;
import com.novax.sdk.pay.withdraw.WithdrawOrderResp;

ReturnResult<WithdrawOrderResp> result = client.execute(
    WithdrawAddOrderRequest.builder()
        .withdrawOrderId("wd-001")
        .type(1)
        .currency("usdt")
        .protocol("TRC20")
        .address("TRecipientAddress...")
        .currencyNumber(new BigDecimal("30.00"))
        .build()
);
```

### Build & test

```bash
cd java/sdk
mvn clean install        # build all modules
mvn test                 # run all tests
mvn -pl sdk-pay test     # run sdk-pay tests only
```

---

## Python

**Requirements:** Python 3.11+

### Install

```bash
pip install novax-sdk
# or from source:
pip install -e python/
```

### Initialize client

```python
from novax_sdk import NovaxClient

client = (
    NovaxClient.builder()
    .endpoint("https://api.novax.dev/api")
    .access_key("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .client_ip("1.2.3.4")   # optional
    .language("zh-CN")       # optional
    .build()
)
```

### H5 Payment — get token

```python
from decimal import Decimal
from novax_sdk.pay.h5 import PayTokenRequest

result = client.execute(
    PayTokenRequest(receipt_order_id="order-001", currency_number=Decimal("100.00"))
)

if result.is_success():
    token = result.data   # str
```

### Receipt — create order

```python
from decimal import Decimal
from novax_sdk.pay.receipt import ReceiptAddOrderRequest

result = client.execute(
    ReceiptAddOrderRequest(
        protocol="TRC20",
        currency="usdt",
        smart_contract_address="TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
        company_user_id="user-123",
        receipt_order_id="order-001",
        currency_number=Decimal("50.00"),
        call_back_url="https://your.domain/callback",
    )
)
```

### Withdraw — create order

```python
from decimal import Decimal
from novax_sdk.pay.withdraw import WithdrawAddOrderRequest

result = client.execute(
    WithdrawAddOrderRequest(
        withdraw_order_id="wd-001",
        order_type=1,
        currency="usdt",
        protocol="TRC20",
        address="TRecipientAddress...",
        currency_number=Decimal("30.00"),
    )
)
```

### Run tests

```bash
cd python
pip install -e ".[dev]"
pytest
```

---

## Go

**Requirements:** Go 1.21+

### Install

```bash
go get novax.dev/sdk
```

### Initialize client

```go
import novax "novax.dev/sdk"

client, err := novax.NewClient(
    novax.WithEndpoint("https://api.novax.dev/api"),
    novax.WithAccessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET"),
    novax.WithClientIP("1.2.3.4"),  // optional
    novax.WithLanguage("zh-CN"),    // optional
)
if err != nil {
    log.Fatal(err)
}
```

### H5 Payment — get token

```go
import (
    "context"
    "github.com/shopspring/decimal"
    novax "novax.dev/sdk"
)

result, err := client.Pay.Token(ctx, &novax.PayTokenRequest{
    ReceiptOrderID: "order-001",
    CurrencyNumber: decimal.NewFromFloat(100.0),
})
if err != nil {
    log.Fatal(err)
}
if result.IsSuccess() {
    token := *result.Data  // string
}
```

### Receipt — create order

```go
result, err := client.Pay.ReceiptAddOrder(ctx, &novax.ReceiptAddOrderRequest{
    Protocol:             "TRC20",
    Currency:             "usdt",
    SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
    CompanyUserID:        "user-123",
    ReceiptOrderID:       "order-001",
    CurrencyNumber:       decimal.NewFromFloat(50.0),
    CallBackURL:          "https://your.domain/callback",
})
```

### Withdraw — create order

```go
result, err := client.Pay.WithdrawAddOrder(ctx, &novax.WithdrawAddOrderRequest{
    WithdrawOrderID:      "wd-001",
    OrderType:            1,
    Currency:             "usdt",
    Protocol:             "TRC20",
    Address:              "TRecipientAddress...",
    CurrencyNumber:       decimal.NewFromFloat(30.0),
})
```

### Available Pay methods

| Method | HTTP | Path |
|---|---|---|
| `Pay.Token` | GET | `/pay/v3/token` |
| `Pay.H5Order` | GET | `/pay/public/h5/order` |
| `Pay.H5Protocols` | GET | `/pay/public/h5/protocols` |
| `Pay.H5Address` | GET | `/pay/public/h5/address` |
| `Pay.H5OkTime` | POST | `/pay/public/h5/ok-time` |
| `Pay.H5Confirm` | POST | `/pay/public/h5/confirm` |
| `Pay.H5OrderStatus` | GET | `/pay/public/h5/order/pay/status` |
| `Pay.DynamicQrPayCreate` | POST | `/pay/v3/qr-code-pay/dynamic/create` |
| `Pay.ReceiptProtocols` | GET | `/pay/v3/protocols` |
| `Pay.ReceiptAddress` | GET | `/pay/v3/receipt/address` |
| `Pay.ReceiptOrders` | GET | `/pay/v3/receipt/orders` |
| `Pay.ReceiptAddOrder` | POST | `/pay/v3/receipt/order/add` |
| `Pay.ReceiptConfirm` | POST | `/pay/v3/receipt/order/confirm` |
| `Pay.WithdrawOrders` | GET | `/pay/v3/withdraw/orders` |
| `Pay.WithdrawAddOrder` | POST | `/pay/v3/withdraw/order/add` |

### Run tests

```bash
cd golang
go test ./...
```

---

## Rust

**Requirements:** Rust 2021 edition

### Add to `Cargo.toml`

```toml
[dependencies]
novax-sdk = { path = "../rust" }  # or version once published
```

### Initialize client

```rust
use novax_sdk::NovaxClient;

let client = NovaxClient::builder()
    .endpoint("https://api.novax.dev/api")
    .access_key("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .client_ip("1.2.3.4")   // optional
    .language("zh-CN")       // optional
    .build()?;
```

### H5 Payment — get token

```rust
use rust_decimal::Decimal;
use novax_sdk::pay::h5::PayTokenRequest;

let result = client.execute(&PayTokenRequest {
    receipt_order_id: "order-001".into(),
    currency_number:  Decimal::new(10000, 2),  // 100.00
})?;

if result.is_success() {
    let token = result.data.unwrap();  // String
}
```

### Receipt — create order

```rust
use novax_sdk::pay::receipt::ReceiptAddOrderRequest;

let result = client.execute(&ReceiptAddOrderRequest {
    protocol:               "TRC20".into(),
    currency:               "usdt".into(),
    smart_contract_address: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t".into(),
    company_user_id:        "user-123".into(),
    receipt_order_id:       "order-001".into(),
    currency_number:        Decimal::new(5000, 2),  // 50.00
    call_back_url:          Some("https://your.domain/callback".into()),
})?;
```

### Withdraw — create order

```rust
use novax_sdk::pay::withdraw::WithdrawAddOrderRequest;

let result = client.execute(&WithdrawAddOrderRequest {
    withdraw_order_id:      "wd-001".into(),
    order_type:             1,
    currency:               "usdt".into(),
    protocol:               "TRC20".into(),
    address:                "TRecipientAddress...".into(),
    currency_number:        Decimal::new(3000, 2),  // 30.00
    call_back_url:          None,
})?;
```

### Run tests

```bash
cd rust
cargo test
```

---

## C#

**Requirements:** .NET 8+

### Add reference

```xml
<ProjectReference Include="../NovaxSdk/NovaxSdk.csproj" />
```

### Initialize client

```csharp
using NovaxSdk;

var client = NovaxClient.Builder()
    .WithEndpoint("https://api.novax.dev/api")
    .WithAccessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .WithClientIp("1.2.3.4")   // optional
    .WithLanguage("zh-CN")      // optional
    .Build();
```

### H5 Payment — get token

```csharp
using NovaxSdk.Pay;

var result = await client.Pay.TokenAsync(
    new PayTokenRequest("order-001", 100.00m)
);

if (result.IsSuccess)
{
    string token = result.Data!;
}
```

### Receipt — create order

```csharp
var result = await client.Pay.ReceiptAddOrderAsync(
    new ReceiptAddOrderRequest
    {
        Protocol             = "TRC20",
        Currency             = "usdt",
        SmartContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
        CompanyUserId        = "user-123",
        ReceiptOrderId       = "order-001",
        CurrencyNumber       = 50.00m,
        CallBackUrl          = "https://your.domain/callback",
    }
);
```

### Withdraw — create order

```csharp
var result = await client.Pay.WithdrawAddOrderAsync(
    new WithdrawAddOrderRequest
    {
        WithdrawOrderId      = "wd-001",
        Type                 = 1,
        Currency             = "usdt",
        Protocol             = "TRC20",
        Address              = "TRecipientAddress...",
        CurrencyNumber       = 30.00m,
    }
);
```

### Run tests

```bash
cd csharp
dotnet test
```

---

## Authentication

All signed endpoints (`/api/pay/v3/*`) require `accessKey` + `accessSecret`. The SDK computes the HMAC-SHA256 signature automatically and adds `X-Access-Key`, `X-Signature`, and `X-Timestamp` headers to every signed request.

Public endpoints (`/pay/public/*`) do not require credentials.

`userId` is resolved server-side from the access key — never pass it from the SDK.

## Dev / Self-signed TLS

To hit a dev server with a self-signed certificate, add `.insecureTls()` (Java/Python/Rust) or `.WithInsecureTls()` (C#) or `novax.WithInsecureTLS()` (Go) to the builder. **Never use in production.**
