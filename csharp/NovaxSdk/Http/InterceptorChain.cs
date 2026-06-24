namespace NovaxSdk.Http;

internal sealed class InterceptorChain(
    IHttpTransport transport,
    IReadOnlyList<IInterceptor> interceptors)
{
    public Task<SdkResponse> ProceedAsync(SdkRequest request, CancellationToken ct)
        => ProceedAtAsync(request, 0, ct);

    private Task<SdkResponse> ProceedAtAsync(SdkRequest request, int index, CancellationToken ct)
    {
        if (index < interceptors.Count)
            return interceptors[index].InterceptAsync(
                request,
                (req, token) => ProceedAtAsync(req, index + 1, token),
                ct);

        return transport.ExecuteAsync(request, ct);
    }
}
