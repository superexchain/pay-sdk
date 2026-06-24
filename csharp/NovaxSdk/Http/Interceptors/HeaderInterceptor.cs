namespace NovaxSdk.Http.Interceptors;

/// <summary>
/// Appends fixed headers (ip, language, …) to every request using
/// multi-value append semantics so both per-request and default values are sent.
/// </summary>
public sealed class HeaderInterceptor(IReadOnlyDictionary<string, string> defaults)
    : IInterceptor
{
    public Task<SdkResponse> InterceptAsync(
        SdkRequest request, RequestHandlerDelegate next, CancellationToken ct)
        => next(request.AppendHeaders(defaults), ct);
}
