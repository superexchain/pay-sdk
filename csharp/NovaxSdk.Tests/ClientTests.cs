using System.Text;

namespace NovaxSdk.Tests;

public class ClientTests
{
    // ── stub transport ────────────────────────────────────────────────────────

    private sealed class StubTransport(string responseBody, int statusCode = 200)
        : IHttpTransport
    {
        public SdkRequest? Captured { get; private set; }

        public Task<SdkResponse> ExecuteAsync(SdkRequest request, CancellationToken ct)
        {
            Captured = request;
            return Task.FromResult(new SdkResponse(
                statusCode,
                new Dictionary<string, IReadOnlyList<string>>(),
                Encoding.UTF8.GetBytes(responseBody)));
        }
    }

    private static NovaxClient BuildClient(
        StubTransport stub,
        Action<NovaxClient.NovaxClientBuilder>? configure = null)
    {
        var builder = NovaxClient.Builder()
            .WithEndpoint("https://api.example.com")
            .WithTransport(stub);
        configure?.Invoke(builder);
        return builder.Build();
    }

    // ── response parsing ──────────────────────────────────────────────────────

    [Fact]
    public async Task Execute_ParsesReturnResult()
    {
        var stub   = new StubTransport("""{"code":200,"msg":"ok","data":"tok-abc123"}""");
        var client = BuildClient(stub);

        var resp = await client.Pay.TokenAsync(new PayTokenRequest("order-1", 1m));

        Assert.True(resp.IsSuccess);
        Assert.Equal("tok-abc123", resp.Data);
    }

    [Fact]
    public async Task Execute_BusinessError_DoesNotThrow()
    {
        var stub   = new StubTransport("""{"code":400,"msg":"invalid order","data":null}""");
        var client = BuildClient(stub);

        var resp = await client.Pay.TokenAsync(new PayTokenRequest("x", 1m));

        Assert.False(resp.IsSuccess);
        Assert.Equal(400, resp.Code);
    }

    // ── request construction ──────────────────────────────────────────────────

    [Fact]
    public async Task Execute_GET_BuildsCorrectUrl()
    {
        var stub   = new StubTransport("""{"code":200,"data":"t"}""");
        var client = BuildClient(stub);

        await client.Pay.TokenAsync(new PayTokenRequest("order-99", 1.5m));

        var req = stub.Captured!;
        Assert.Equal("GET", req.HttpMethod);
        Assert.Contains("receiptOrderId=order-99", req.Url);
        Assert.Contains("currencyNumber=1.5", req.Url);
    }

    [Fact]
    public async Task Execute_POST_SetsContentType()
    {
        var stub   = new StubTransport("""{"code":200,"data":null}""");
        var client = BuildClient(stub);

        await client.Pay.ReceiptAddOrderAsync(new ReceiptAddOrderRequest(
            Protocol: "TRC20", Currency: "USDT",
            SmartContractAddress: "Taddr", CompanyUserId: "888",
            ReceiptOrderId: "r-1", CurrencyNumber: 1m));

        var req = stub.Captured!;
        Assert.Equal("POST", req.HttpMethod);
        Assert.True(req.Headers.TryGetValue("Content-Type", out var ct));
        Assert.Equal("application/json", ct[0]);
    }

    // ── header injection ──────────────────────────────────────────────────────

    [Fact]
    public async Task Execute_InjectsIpAndLanguage()
    {
        var stub   = new StubTransport("""{"code":200,"data":"t"}""");
        var client = BuildClient(stub, b => b.WithClientIp("1.2.3.4").WithLanguage("zh-CN"));

        await client.Pay.TokenAsync(new PayTokenRequest("o", 1m));

        var h = stub.Captured!.Headers;
        Assert.Equal("1.2.3.4", h["ip"][0]);
        Assert.Equal("zh-CN",   h["language"][0]);
    }

    [Fact]
    public async Task Execute_NoUserIdHeader()
    {
        // userId is injected server-side from X-Access-Key — the SDK must never send it.
        var stub   = new StubTransport("""{"code":200,"data":"t"}""");
        var client = BuildClient(stub);

        await client.Pay.TokenAsync(new PayTokenRequest("o", 1m));

        Assert.False(stub.Captured!.Headers.ContainsKey("userId"));
    }

    // ── signature ─────────────────────────────────────────────────────────────

    [Fact]
    public async Task Execute_SignedPath_SetsSignatureHeaders()
    {
        var stub   = new StubTransport("""{"code":200,"data":"t"}""");
        var client = BuildClient(stub, b => b.WithAccessKey("ak-test", "sk-test"));

        await client.Pay.TokenAsync(new PayTokenRequest("o", 1m));

        var h = stub.Captured!.Headers;
        Assert.Equal("ak-test", h["X-Access-Key"][0]);
        Assert.Equal(64, h["X-Signature"][0].Length);
        Assert.NotEmpty(h["X-Timestamp"][0]);
    }

    [Fact]
    public async Task Execute_PublicPath_NoSignatureHeaders()
    {
        var stub   = new StubTransport("""{"code":200,"data":null}""");
        var client = BuildClient(stub, b => b.WithAccessKey("ak-test", "sk-test"));

        // /pay/public/h5/order does NOT match /api/pay/v3/*
        await client.Pay.H5OrderAsync(new H5OrderRequest("tok"));

        Assert.False(stub.Captured!.Headers.ContainsKey("X-Access-Key"));
    }
}
