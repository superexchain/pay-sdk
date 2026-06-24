namespace NovaxSdk.Http;

/// <summary>Wire-level response handed back through the interceptor chain.</summary>
public sealed record SdkResponse(
    int StatusCode,
    IReadOnlyDictionary<string, IReadOnlyList<string>> Headers,
    byte[] Body);
