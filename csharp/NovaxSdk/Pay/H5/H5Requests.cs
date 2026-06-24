namespace NovaxSdk.Pay.H5;

/// <summary>GET /pay/v3/token — userId injected server-side; callers never supply it.</summary>
public sealed record PayTokenRequest(string ReceiptOrderId, decimal CurrencyNumber)
    : IApiRequest<string>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/v3/token";
    public IReadOnlyDictionary<string, string>? QueryParams => new Dictionary<string, string>
    {
        ["receiptOrderId"] = ReceiptOrderId,
        ["currencyNumber"]  = CurrencyNumber.ToString("G")
    };
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>GET /pay/public/h5/order</summary>
public sealed record H5OrderRequest(string? Token = null) : IApiRequest<PayOrderResp>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/public/h5/order";
    public IReadOnlyDictionary<string, string>? QueryParams =>
        QueryHelper.Optional(("token", Token));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>GET /pay/public/h5/protocols</summary>
public sealed record H5ProtocolsRequest(string? Token = null)
    : IApiRequest<IReadOnlyList<PayProtocolsResp>>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/public/h5/protocols";
    public IReadOnlyDictionary<string, string>? QueryParams =>
        QueryHelper.Optional(("token", Token));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>GET /pay/public/h5/address</summary>
public sealed record H5AddressRequest(
    string? Protocol             = null,
    string? Currency             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null,
    string? Token                = null) : IApiRequest<PayOrderAddressResp>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/public/h5/address";
    public IReadOnlyDictionary<string, string>? QueryParams => QueryHelper.Optional(
        ("protocol",             Protocol),
        ("currency",             Currency),
        ("smartContractAddress", SmartContractAddress),
        ("companyUserId",        CompanyUserId),
        ("token",                Token));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>POST /pay/public/h5/ok-time</summary>
public sealed record H5OkTimeRequest(
    string? Protocol             = null,
    string? Currency             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null,
    string? Token                = null) : IApiRequest<bool>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/public/h5/ok-time";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        protocol             = Protocol,
        currency             = Currency,
        smartContractAddress = SmartContractAddress,
        companyUserId        = CompanyUserId,
        token                = Token
    };
}

/// <summary>POST /pay/public/h5/confirm</summary>
public sealed record H5ConfirmRequest(
    string? Protocol             = null,
    string? Currency             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null,
    string? Token                = null) : IApiRequest<object?>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/public/h5/confirm";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        protocol             = Protocol,
        currency             = Currency,
        smartContractAddress = SmartContractAddress,
        companyUserId        = CompanyUserId,
        token                = Token
    };
}

/// <summary>GET /pay/public/h5/order/pay/status</summary>
public sealed record H5OrderStatusRequest(
    string? Protocol             = null,
    string? Currency             = null,
    string? SmartContractAddress = null,
    string? CompanyUserId        = null,
    string? Token                = null) : IApiRequest<PayOrderAddressResp>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/public/h5/order/pay/status";
    public IReadOnlyDictionary<string, string>? QueryParams => QueryHelper.Optional(
        ("protocol",             Protocol),
        ("currency",             Currency),
        ("smartContractAddress", SmartContractAddress),
        ("companyUserId",        CompanyUserId),
        ("token",                Token));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}
