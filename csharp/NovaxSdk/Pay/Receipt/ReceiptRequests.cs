namespace NovaxSdk.Pay.Receipt;

/// <summary>GET /pay/v3/protocols — ReceiptType: 1=dynamic, 6=fixed (default 1).</summary>
public sealed record ReceiptProtocolsRequest(int ReceiptType = 1)
    : IApiRequest<IReadOnlyList<PayProtocolsResp>>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/v3/protocols";
    public IReadOnlyDictionary<string, string>? QueryParams =>
        new Dictionary<string, string> { ["type"] = ReceiptType.ToString() };
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>GET /pay/v3/receipt/address</summary>
public sealed record ReceiptAddressRequest(
    string? Protocol             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null) : IApiRequest<PayOrderAddressFixedResp>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/v3/receipt/address";
    public IReadOnlyDictionary<string, string>? QueryParams => QueryHelper.Optional(
        ("protocol",             Protocol),
        ("smartContractAddress", SmartContractAddress),
        ("companyUserId",        CompanyUserId));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>GET /pay/v3/receipt/orders</summary>
public sealed record ReceiptOrdersRequest(
    string? ReceiptOrderIds = null,
    int?    OrderStatus     = null,
    int?    Status          = null) : IApiRequest<IReadOnlyList<ReceiptOrderResp>>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/v3/receipt/orders";
    public IReadOnlyDictionary<string, string>? QueryParams => QueryHelper.Optional(
        ("receiptOrderIds", ReceiptOrderIds),
        ("orderStatus",     OrderStatus?.ToString()),
        ("status",          Status?.ToString()));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>POST /pay/v3/receipt/order/add</summary>
public sealed record ReceiptAddOrderRequest(
    string?  Protocol             = null,
    string?  Currency             = null,
    string?  SmartContractAddress = null,
    string?  CompanyUserId        = null,
    string?  ReceiptOrderId       = null,
    decimal? CurrencyNumber       = null,
    string?  CallBackUrl          = null) : IApiRequest<ReceiptOrderAddressResp>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/v3/receipt/order/add";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        protocol             = Protocol,
        currency             = Currency,
        smartContractAddress = SmartContractAddress,
        companyUserId        = CompanyUserId,
        receiptOrderId       = ReceiptOrderId,
        currencyNumber       = CurrencyNumber?.ToString("G"),
        callBackUrl          = CallBackUrl
    };
}

/// <summary>POST /pay/v3/receipt/order/confirm</summary>
public sealed record ReceiptConfirmRequest(
    string? Protocol             = null,
    string? Currency             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null,
    string? ReceiptOrderId       = null) : IApiRequest<object?>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/v3/receipt/order/confirm";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        protocol             = Protocol,
        currency             = Currency,
        smartContractAddress = SmartContractAddress,
        companyUserId        = CompanyUserId,
        receiptOrderId       = ReceiptOrderId
    };
}
