# Novax SDK

Novax Pay API 的多语言 SDK，支持 Java、Python、Go、Rust 和 C#。

所有 SDK 遵循相同的设计：单一客户端入口、每个接口对应一个请求对象、统一的 `ReturnResult<T>` 响应封装。

## 语言支持

| 语言 | 最低版本 | 包名 |
|---|---|---|
| [Java](#java) | Java 17 | Maven (`com.novax:sdk-pay`) |
| [Python](#python) | Python 3.11 | pip (`novax-sdk`) |
| [Go](#go) | Go 1.21 | `novax.dev/sdk` |
| [Rust](#rust) | Rust 2021 | Cargo (`novax-sdk`) |
| [C#](#c) | .NET 8 | NuGet (`NovaxSdk`) |

---

## 响应结构

所有接口返回 `ReturnResult<T>`：

| 字段 | 类型 | 说明 |
|---|---|---|
| `code` | int | `200` 表示成功 |
| `msg` | string | 可读的提示信息 |
| `data` | T | 类型化数据（失败时为 null） |

---

## Java

**环境要求：** Java 17、Maven

### 添加依赖（`pom.xml`）

```xml
<dependency>
    <groupId>com.novax</groupId>
    <artifactId>sdk-pay</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 初始化客户端

```java
import com.novax.sdk.core.NovaxClient;

NovaxClient client = NovaxClient.builder()
    .endpoint("https://api.novax.dev/api")
    .accessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .clientIp("1.2.3.4")   // 可选：IP 白名单请求头
    .language("zh-CN")      // 可选：语言请求头
    .build();
```

### H5 支付 — 获取 Token

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

### 收款 — 创建订单

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

### 提现 — 创建订单

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

### 构建与测试

```bash
cd java/sdk
mvn clean install        # 构建所有模块
mvn test                 # 运行所有测试
mvn -pl sdk-pay test     # 仅运行 sdk-pay 测试
```

---

## Python

**环境要求：** Python 3.11+

### 安装

```bash
pip install novax-sdk
# 或从源码安装：
pip install -e python/
```

### 初始化客户端

```python
from novax_sdk import NovaxClient

client = (
    NovaxClient.builder()
    .endpoint("https://api.novax.dev/api")
    .access_key("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .client_ip("1.2.3.4")   # 可选
    .language("zh-CN")       # 可选
    .build()
)
```

### H5 支付 — 获取 Token

```python
from decimal import Decimal
from novax_sdk.pay.h5 import PayTokenRequest

result = client.execute(
    PayTokenRequest(receipt_order_id="order-001", currency_number=Decimal("100.00"))
)

if result.is_success():
    token = result.data   # str
```

### 收款 — 创建订单

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

### 提现 — 创建订单

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

### 运行测试

```bash
cd python
pip install -e ".[dev]"
pytest
```

---

## Go

**环境要求：** Go 1.21+

### 安装

```bash
go get novax.dev/sdk
```

### 初始化客户端

```go
import novax "novax.dev/sdk"

client, err := novax.NewClient(
    novax.WithEndpoint("https://api.novax.dev/api"),
    novax.WithAccessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET"),
    novax.WithClientIP("1.2.3.4"),  // 可选
    novax.WithLanguage("zh-CN"),    // 可选
)
if err != nil {
    log.Fatal(err)
}
```

### H5 支付 — 获取 Token

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

### 收款 — 创建订单

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

### 提现 — 创建订单

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

### Pay 接口列表

| 方法 | HTTP 方法 | 路径 |
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

### 运行测试

```bash
cd golang
go test ./...
```

---

## Rust

**环境要求：** Rust 2021 edition

### 添加依赖（`Cargo.toml`）

```toml
[dependencies]
novax-sdk = { path = "../rust" }  # 发布后改为 version
```

### 初始化客户端

```rust
use novax_sdk::NovaxClient;

let client = NovaxClient::builder()
    .endpoint("https://api.novax.dev/api")
    .access_key("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .client_ip("1.2.3.4")   // 可选
    .language("zh-CN")       // 可选
    .build()?;
```

### H5 支付 — 获取 Token

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

### 收款 — 创建订单

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

### 提现 — 创建订单

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

### 运行测试

```bash
cd rust
cargo test
```

---

## C#

**环境要求：** .NET 8+

### 添加引用

```xml
<ProjectReference Include="../NovaxSdk/NovaxSdk.csproj" />
```

### 初始化客户端

```csharp
using NovaxSdk;

var client = NovaxClient.Builder()
    .WithEndpoint("https://api.novax.dev/api")
    .WithAccessKey("YOUR_ACCESS_KEY", "YOUR_ACCESS_SECRET")
    .WithClientIp("1.2.3.4")   // 可选
    .WithLanguage("zh-CN")      // 可选
    .Build();
```

### H5 支付 — 获取 Token

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

### 收款 — 创建订单

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

### 提现 — 创建订单

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

### 运行测试

```bash
cd csharp
dotnet test
```

---

## 认证鉴权

所有签名接口（`/api/pay/v3/*`）需要提供 `accessKey` 和 `accessSecret`。SDK 会自动计算 HMAC-SHA256 签名，并在每次请求时自动添加 `X-Access-Key`、`X-Signature`、`X-Timestamp` 请求头。

公开接口（`/pay/public/*`）不需要凭据。

`userId` 由服务端根据 `X-Access-Key` 解析注入，SDK 端不需要也不应传递此字段。

## 开发环境 / 自签名证书

访问使用自签名证书的开发服务器时，在构建器中添加：

| 语言 | 方法 |
|---|---|
| Java / Python / Rust | `.insecureTls()` |
| C# | `.WithInsecureTls()` |
| Go | `novax.WithInsecureTLS()` |

**生产环境严禁使用此选项。**
