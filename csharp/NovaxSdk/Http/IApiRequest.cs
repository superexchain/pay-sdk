namespace NovaxSdk.Http;

/// <summary>
/// Self-describing API request. Implement one record per endpoint.
/// <see cref="NovaxClient"/> is the sole entry point via
/// <see cref="Pay.PayClient"/> methods.
/// </summary>
public interface IApiRequest<TResponse>
{
    string HttpMethod { get; }
    string Path { get; }
    IReadOnlyDictionary<string, string>? QueryParams { get; }
    IReadOnlyDictionary<string, string>? ExtraHeaders { get; }
    object? Body { get; }
}
