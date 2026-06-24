using NovaxSdk.Http;

namespace NovaxSdk.Pay.Qrcode;

/// <summary>POST /pay/v3/qr-code-pay/dynamic/create</summary>
public sealed record DynamicQrPayCreateRequest(
    string  ReceiptOrderId,
    long    CompanyUserId,
    string  Currency,
    decimal Amount,
    string? Comment = null) : IApiRequest<string>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/v3/qr-code-pay/dynamic/create";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        receiptOrderId = ReceiptOrderId,
        companyUserId  = CompanyUserId,
        currency       = Currency,
        amount         = Amount.ToString("G"),
        comment        = Comment
    };
}
