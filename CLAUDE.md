# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

Multi-language SDK monorepo: `csharp/`, `golang/`, `java/`, `python/`, `rust/`. Only `java/` is scaffolded; the others are empty placeholders.

## Java SDK (`java/sdk/`)

Java 17 Maven multi-module project. **No Spring dependency** — only Jackson + Lombok + JDK 17. Group ID `com.novax`.

| Module | Purpose |
|---|---|
| `sdk-core` | Transport, interceptor chain, signing, JSON — no business knowledge |
| `sdk-pay` | Pay API Request/Response pairs; depends on `sdk-core` |

### Architecture

**Request-driven**: every API endpoint is a `XxxRequest extends AbstractApiRequest<XxxResponse>` class. `NovaxClient.execute(request)` is the only entry point and returns `ReturnResult<R>`.

```
NovaxClient
  └── InterceptorChain
        ├── HeaderInterceptor      (ip, language defaults — appendHeaders)
        ├── SignatureInterceptor   (X-Access-Key / X-Signature / X-Timestamp — setHeaders)
        └── LoggingInterceptor     (INFO normal, SEVERE on HTTP ≥500 with body)
  └── JdkHttpTransport (java.net.http.HttpClient; swap via HttpTransport interface)
```

**Signature scheme** mirrors server-side `SignatureFilter`:
- Only `/api/pay/v3/*` paths are signed; `/pay/public/*` pass through
- `dataToSign = METHOD[&sorted-query][&sorted-body]&timestamp=<ms>`; HMAC-SHA256 hex
- `userId` is **server-injected** from `X-Access-Key` lookup — never send it from the SDK

**Adding a new endpoint**: add `XxxRequest + XxxResponse` in the relevant `sdk-pay` sub-package. Touch nothing else.

**Shared response models** live in `sdk-pay/.../pay/model/` — reused across multiple endpoints (`PayProtocolsResp`, `ReceiptOrderResp`, `WithdrawOrderResp`, …).

### Common commands

All Maven commands run from `java/sdk/`:

```bash
# Build all modules
mvn clean install

# Build a single module (and its deps)
mvn -pl sdk-pay -am clean install

# Run all tests
mvn test

# Run tests in one module
mvn -pl sdk-pay test

# Run a single test class
mvn -pl sdk-pay test -Dtest=PayTokenRequestTest
```

No Maven wrapper — use a locally installed `mvn`.

### Integration tests

Tests in `sdk-pay/src/test/` hit the real dev server (`api.novax.dev`). The `NovaxClient` is initialised once as `private static final CLIENT` in each test class. Tests use `.insecureTls()` for the self-signed dev cert.

### Key config points

- `ReturnResult.isSuccess()` checks `code == 200`
- `SignatureInterceptor.shouldSign()` path prefix is `/api/pay/v3/` — update when server adds new signed prefixes
- `LoggingInterceptor`: HTTP ≥500 logs at `SEVERE` with response body (truncated 2KB); business failures (`!isSuccess()`) log at `WARNING` in `NovaxClient`

## Cross-language consistency

When implementing sibling languages, mirror the `com.novax.sdk.*` package naming and the same request-driven pattern (one Request class per endpoint, single client entry point).
