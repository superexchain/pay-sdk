using NovaxSdk.Pay.H5;
using NovaxSdk.Pay.Model;
using NovaxSdk.Pay.Qrcode;
using NovaxSdk.Pay.Receipt;
using NovaxSdk.Pay.Withdraw;

namespace NovaxSdk.Pay;

/// <summary>
/// Typed methods for all pay API endpoints.
/// Access via <see cref="NovaxClient.Pay"/>.
/// </summary>
public sealed class PayClient(NovaxClient client)
{
    // ── H5 ───────────────────────────────────────────────────────────────────

    /// <summary>GET /pay/v3/token</summary>
    public Task<ReturnResult<string>> TokenAsync(
        PayTokenRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/public/h5/order</summary>
    public Task<ReturnResult<PayOrderResp>> H5OrderAsync(
        H5OrderRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/public/h5/protocols</summary>
    public Task<ReturnResult<IReadOnlyList<PayProtocolsResp>>> H5ProtocolsAsync(
        H5ProtocolsRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/public/h5/address</summary>
    public Task<ReturnResult<PayOrderAddressResp>> H5AddressAsync(
        H5AddressRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>POST /pay/public/h5/ok-time</summary>
    public Task<ReturnResult<bool>> H5OkTimeAsync(
        H5OkTimeRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>POST /pay/public/h5/confirm</summary>
    public Task<ReturnResult<object?>> H5ConfirmAsync(
        H5ConfirmRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/public/h5/order/pay/status</summary>
    public Task<ReturnResult<PayOrderAddressResp>> H5OrderStatusAsync(
        H5OrderStatusRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    // ── QRCode ───────────────────────────────────────────────────────────────

    /// <summary>POST /pay/v3/qr-code-pay/dynamic/create</summary>
    public Task<ReturnResult<string>> DynamicQrPayCreateAsync(
        DynamicQrPayCreateRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    // ── Receipt ──────────────────────────────────────────────────────────────

    /// <summary>GET /pay/v3/protocols</summary>
    public Task<ReturnResult<IReadOnlyList<PayProtocolsResp>>> ReceiptProtocolsAsync(
        ReceiptProtocolsRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/v3/receipt/address</summary>
    public Task<ReturnResult<PayOrderAddressFixedResp>> ReceiptAddressAsync(
        ReceiptAddressRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>GET /pay/v3/receipt/orders</summary>
    public Task<ReturnResult<IReadOnlyList<ReceiptOrderResp>>> ReceiptOrdersAsync(
        ReceiptOrdersRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>POST /pay/v3/receipt/order/add</summary>
    public Task<ReturnResult<ReceiptOrderAddressResp>> ReceiptAddOrderAsync(
        ReceiptAddOrderRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>POST /pay/v3/receipt/order/confirm</summary>
    public Task<ReturnResult<object?>> ReceiptConfirmAsync(
        ReceiptConfirmRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    // ── Withdraw ─────────────────────────────────────────────────────────────

    /// <summary>GET /pay/v3/withdraw/orders</summary>
    public Task<ReturnResult<IReadOnlyList<WithdrawOrderResp>>> WithdrawOrdersAsync(
        WithdrawOrdersRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);

    /// <summary>POST /pay/v3/withdraw/order/add</summary>
    public Task<ReturnResult<WithdrawOrderResp>> WithdrawAddOrderAsync(
        WithdrawAddOrderRequest request, CancellationToken ct = default)
        => client.ExecuteAsync(request, ct);
}
