using System.Text.Json;
using NovaxSdk.Http.Interceptors;
using NovaxSdk.Pay.H5;
using NovaxSdk.Pay.Qrcode;
using NovaxSdk.Pay.Receipt;
using NovaxSdk.Pay.Withdraw;
using Xunit.Abstractions;

namespace NovaxSdk.Tests.Pay;

public class PayIntegrationTests(ITestOutputHelper output)
{
    private const string AccessKey    = "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD";
    private const string AccessSecret = "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2";

    private static readonly Lazy<NovaxClient> LazyClient = new(() =>
        NovaxClient.Builder()
            .WithEndpoint("https://api.novax.dev/api")
            .WithAccessKey(AccessKey, AccessSecret)
            .WithClientIp("1.2.3.4")
            .WithLanguage("zh-CN")
            .AddInterceptor(new LoggingInterceptor())
            .WithInsecureTls()
            .Build());

    private static NovaxClient Client => LazyClient.Value;

    private static readonly JsonSerializerOptions JsonOpts = new() { WriteIndented = false };

    private void Log<T>(ReturnResult<T> resp) =>
        output.WriteLine(JsonSerializer.Serialize(resp, JsonOpts));

    private static async Task<string> GetTokenAsync()
    {
        var resp = await Client.Pay.TokenAsync(new PayTokenRequest("1234", 1m));
        Assert.True(resp.IsSuccess, $"TokenAsync failed: {resp.Msg}");
        return resp.Data!;
    }

    private static string Ts() =>
        DateTimeOffset.UtcNow.ToUnixTimeMilliseconds().ToString();

    // ── H5 ───────────────────────────────────────────────────────────────────

    [Fact] public async Task PayToken()
    {
        var resp = await Client.Pay.TokenAsync(new PayTokenRequest("1234", 1m));
        Log(resp);
        Assert.True(resp.IsSuccess, resp.Msg);
        Assert.NotEmpty(resp.Data!);
    }

    [Fact] public async Task H5Order()
    {
        var resp = await Client.Pay.H5OrderAsync(new H5OrderRequest(await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task H5Protocols()
    {
        var resp = await Client.Pay.H5ProtocolsAsync(new H5ProtocolsRequest(await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task H5Address()
    {
        var resp = await Client.Pay.H5AddressAsync(new H5AddressRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", Token: await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task H5OkTime()
    {
        var resp = await Client.Pay.H5OkTimeAsync(new H5OkTimeRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", Token: await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task H5Confirm()
    {
        var resp = await Client.Pay.H5ConfirmAsync(new H5ConfirmRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", Token: await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task H5OrderStatus()
    {
        var resp = await Client.Pay.H5OrderStatusAsync(new H5OrderStatusRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", Token: await GetTokenAsync()));
        Log(resp);
        Assert.NotNull(resp);
    }

    // ── QRCode ────────────────────────────────────────────────────────────────

    [Fact] public async Task DynamicQrPayCreate()
    {
        var resp = await Client.Pay.DynamicQrPayCreateAsync(new DynamicQrPayCreateRequest(
            ReceiptOrderId: Ts(), CompanyUserId: 88_888_896L,
            Currency: "usdt", Amount: 1m, Comment: "test"));
        Log(resp);
        Assert.NotNull(resp?.Msg); // server returns order id in Msg
    }

    // ── Receipt ───────────────────────────────────────────────────────────────

    [Fact] public async Task ReceiptProtocolsDynamic()
    {
        var resp = await Client.Pay.ReceiptProtocolsAsync(new ReceiptProtocolsRequest(ReceiptType: 1));
        Log(resp);
        Assert.True(resp.IsSuccess, resp.Msg);
    }

    [Fact] public async Task ReceiptProtocolsFixed()
    {
        var resp = await Client.Pay.ReceiptProtocolsAsync(new ReceiptProtocolsRequest(ReceiptType: 6));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task ReceiptAddress()
    {
        var resp = await Client.Pay.ReceiptAddressAsync(new ReceiptAddressRequest(
            Protocol: "TRC20",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896"));
        Log(resp);
        Assert.NotNull(resp);
    }

    [Fact] public async Task ReceiptOrders()
    {
        var resp = await Client.Pay.ReceiptOrdersAsync(new ReceiptOrdersRequest());
        Log(resp);
        Assert.True(resp.IsSuccess, resp.Msg);
    }

    [Fact] public async Task ReceiptAddOrder()
    {
        var resp = await Client.Pay.ReceiptAddOrderAsync(new ReceiptAddOrderRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", ReceiptOrderId: Ts(), CurrencyNumber: 1m,
            CallBackUrl: "http://tripartite-payment-ts.dev.svc.cluster.local/v3/public/test/call/back"));
        Log(resp);
        Assert.True(resp.IsSuccess, resp.Msg);
    }

    [Fact] public async Task ReceiptConfirm()
    {
        var resp = await Client.Pay.ReceiptConfirmAsync(new ReceiptConfirmRequest(
            Protocol: "TRC20", Currency: "usdt",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            CompanyUserId: "88888896", ReceiptOrderId: "1234"));
        Log(resp);
        Assert.NotNull(resp);
    }

    // ── Withdraw ──────────────────────────────────────────────────────────────

    [Fact] public async Task WithdrawOrders()
    {
        var resp = await Client.Pay.WithdrawOrdersAsync(
            new WithdrawOrdersRequest(WithdrawOrderIds: "w_1234,1745564918818000"));
        Log(resp);
        Assert.True(resp.IsSuccess, resp.Msg);
    }

    [Fact] public async Task WithdrawAddOrder()
    {
        var resp = await Client.Pay.WithdrawAddOrderAsync(new WithdrawAddOrderRequest(
            WithdrawOrderId: Ts(), OrderType: 1, Currency: "usdt", Protocol: "TRC20",
            SmartContractAddress: "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
            Address: "TVKUpYxUV4LTdFZ24kNrvMm6phXx6vv7Zc",
            CurrencyNumber: 1m, CallBackUrl: "https://example.com/callback"));
        Log(resp);
        Assert.NotNull(resp);
    }
}
