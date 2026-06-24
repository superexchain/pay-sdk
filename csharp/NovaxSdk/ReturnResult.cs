namespace NovaxSdk;

/// <summary>Server-side response envelope. Code == 200 indicates success.</summary>
public sealed record ReturnResult<T>(
    int? Code,
    string? Msg,
    T? Data)
{
    public bool IsSuccess => Code == 200;
}
