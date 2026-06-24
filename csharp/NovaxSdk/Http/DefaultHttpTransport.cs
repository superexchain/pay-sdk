using System.Net.Http.Headers;
using System.Net.Security;

namespace NovaxSdk.Http;

internal sealed class DefaultHttpTransport : IHttpTransport, IDisposable
{
    private readonly HttpClient _httpClient;

    public DefaultHttpTransport(TimeSpan timeout, bool insecureTls)
    {
        var handler = new HttpClientHandler();
        if (insecureTls)
            handler.ServerCertificateCustomValidationCallback =
                HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;

        _httpClient = new HttpClient(handler) { Timeout = timeout };
    }

    public async Task<SdkResponse> ExecuteAsync(SdkRequest request, CancellationToken ct)
    {
        using var req = new HttpRequestMessage(
            new System.Net.Http.HttpMethod(request.HttpMethod), request.Url);

        foreach (var (key, values) in request.Headers)
        {
            foreach (var value in values)
                req.Headers.TryAddWithoutValidation(key, value);
        }

        if (request.Body is not null)
        {
            req.Content = new ByteArrayContent(request.Body);
            req.Content.Headers.ContentType =
                new MediaTypeHeaderValue("application/json");
        }

        using var resp = await _httpClient.SendAsync(req, ct);
        var body = await resp.Content.ReadAsByteArrayAsync(ct);

        var headers = resp.Headers.Concat(resp.Content.Headers)
            .ToDictionary(
                h => h.Key,
                h => (IReadOnlyList<string>)h.Value.ToList().AsReadOnly(),
                StringComparer.OrdinalIgnoreCase);

        return new SdkResponse((int)resp.StatusCode, headers, body);
    }

    public void Dispose() => _httpClient.Dispose();
}
