namespace NovaxSdk.Pay.Model;

public record PayProtocolsResp(
    string? Currency,
    string? Protocol,
    int? CurrencyType,
    string? ChainName,
    string? SmartContractAddress);

public record PayOrderResp(
    string? ShortName,
    string? Logo,
    string? Currency,
    decimal? CurrencyNumber,
    decimal? PayCurrencyNumber);

public record PayOrderAddressResp(
    string? Protocol,
    string? Currency,
    string? ReceiptAddress,
    long? EndTime,
    int? Status);

public record PayOrderAddressFixedResp(
    string? Protocol,
    string? ReceiptAddress);

public record ReceiptHashOrderResp(
    long? Id,
    long? UserId,
    long? UserCurrencyOrderId,
    string? CompanyOrderId,
    string? Protocol,
    string? Currency,
    string? FromAddress,
    string? ToAddress,
    string? TxId,
    decimal? Amount,
    int? Status,
    long? SuccessTime,
    long? CreateTime,
    int? Type,
    decimal? Fee);

public record ReceiptOrderResp(
    string? ReceiptOrderId,
    string? Currency,
    string? Protocol,
    string? SmartContractAddress,
    decimal? CurrencyNumber,
    decimal? AcceptCurrencyNumber,
    long? Counts,
    long? OkTime,
    int? OrderStatus,
    int? Status,
    long? EndTime,
    long? LoseTime,
    IReadOnlyList<ReceiptHashOrderResp>? HashOrders);

public record ReceiptOrderAddressResp(
    string? ReceiptOrderId,
    string? Currency,
    string? Protocol,
    string? SmartContractAddress,
    decimal? CurrencyNumber,
    decimal? AcceptCurrencyNumber,
    long? Counts,
    long? OkTime,
    int? OrderStatus,
    int? Status,
    long? EndTime,
    long? LoseTime,
    IReadOnlyList<ReceiptHashOrderResp>? HashOrders,
    string? Address);

public record WithdrawOrderResp(
    string? WithdrawOrderId,
    int? Type,
    string? Currency,
    string? Protocol,
    string? SmartContractAddress,
    string? Address,
    decimal? CurrencyNumber,
    int? Status,
    IReadOnlyList<ReceiptHashOrderResp>? HashOrders);
