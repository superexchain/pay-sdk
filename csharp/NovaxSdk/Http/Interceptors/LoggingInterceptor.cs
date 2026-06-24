using System.Diagnostics;
using System.Text;

namespace NovaxSdk.Http.Interceptors;

/// <summary>
/// Logs outbound requests and inbound responses via <see cref="Debug.WriteLine"/>.
/// HTTP ≥500 logs at Error level with response body (truncated to 2 KB).
/// </summary>
public sealed class LoggingInterceptor : IInterceptor
{
    private const int ErrorBodyMaxChars = 2048;

    public async Task<SdkResponse> InterceptAsync(
        SdkRequest request, RequestHandlerDelegate next, CancellationToken ct)
    {
        Debug.WriteLine($"-> {request.HttpMethod} {request.Url}");
        var sw = Stopwatch.StartNew();

        var resp = await next(request, ct);
        sw.Stop();

        if (resp.StatusCode >= 500)
        {
            var body = Encoding.UTF8.GetString(resp.Body);
            if (body.Length > ErrorBodyMaxChars) body = body[..ErrorBodyMaxChars] + "…";
            Debug.WriteLine(
                $"<- {resp.StatusCode} ({sw.ElapsedMilliseconds}ms) body={body}");
        }
        else
        {
            Debug.WriteLine($"<- {resp.StatusCode} ({sw.ElapsedMilliseconds}ms)");
        }

        return resp;
    }
}
