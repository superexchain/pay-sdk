namespace NovaxSdk.Http;

/// <summary>
/// Delegate representing the next handler in the interceptor chain.
/// </summary>
public delegate Task<SdkResponse> RequestHandlerDelegate(
    SdkRequest request, CancellationToken cancellationToken);

/// <summary>
/// Cross-cutting hook for outbound requests and inbound responses.
/// Call <paramref name="next"/> exactly once to continue the chain.
/// </summary>
public interface IInterceptor
{
    Task<SdkResponse> InterceptAsync(
        SdkRequest request,
        RequestHandlerDelegate next,
        CancellationToken cancellationToken);
}
