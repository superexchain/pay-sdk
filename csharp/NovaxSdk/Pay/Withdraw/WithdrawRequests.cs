namespace NovaxSdk.Pay.Withdraw;

/// <summary>GET /pay/v3/withdraw/orders</summary>
public sealed record WithdrawOrdersRequest(
    string? WithdrawOrderIds = null,
    int?    Status           = null) : IApiRequest<IReadOnlyList<WithdrawOrderResp>>
{
    public string HttpMethod => "GET";
    public string Path       => "/pay/v3/withdraw/orders";
    public IReadOnlyDictionary<string, string>? QueryParams => QueryHelper.Optional(
        ("withdrawOrderIds", WithdrawOrderIds),
        ("status",           Status?.ToString()));
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => null;
}

/// <summary>POST /pay/v3/withdraw/order/add</summary>
public sealed record WithdrawAddOrderRequest(
    string?  WithdrawOrderId      = null,
    int?     OrderType            = null,
    string?  Currency             = null,
    string?  Protocol             = null,
    string?  SmartContractAddress = null,
    string?  Address              = null,
    decimal? CurrencyNumber       = null,
    string?  CallBackUrl          = null) : IApiRequest<WithdrawOrderResp>
{
    public string HttpMethod => "POST";
    public string Path       => "/pay/v3/withdraw/order/add";
    public IReadOnlyDictionary<string, string>? QueryParams  => null;
    public IReadOnlyDictionary<string, string>? ExtraHeaders => null;
    public object? Body => new
    {
        withdrawOrderId      = WithdrawOrderId,
        type                 = OrderType,
        currency             = Currency,
        protocol             = Protocol,
        smartContractAddress = SmartContractAddress,
        address              = Address,
        currencyNumber       = CurrencyNumber?.ToString("G"),
        callBackUrl          = CallBackUrl
    };
}
