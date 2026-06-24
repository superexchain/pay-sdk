namespace NovaxSdk.Http;

/// <summary>
/// Pluggable HTTP transport. The default implementation uses
/// <see cref="System.Net.Http.HttpClient"/>.
/// Implement this interface to swap in a custom transport or a test stub.
/// </summary>
public interface IHttpTransport
{
    Task<SdkResponse> ExecuteAsync(SdkRequest request, CancellationToken cancellationToken);
}
