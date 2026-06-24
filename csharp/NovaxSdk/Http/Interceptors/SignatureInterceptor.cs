namespace NovaxSdk.Http.Interceptors;

/// <summary>
/// Stamps X-Access-Key / X-Signature / X-Timestamp on signed paths.
/// Only /api/pay/v3/* is signed; /pay/public/* passes through.
/// </summary>
public sealed class SignatureInterceptor(AccessKeyCredentials credentials)
    : IInterceptor
{
    public async Task<SdkResponse> InterceptAsync(
        SdkRequest request, RequestHandlerDelegate next, CancellationToken ct)
    {
        if (!SignatureCodec.ShouldSign(new Uri(request.Url).AbsolutePath))
            return await next(request, ct);

        var ts   = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
        var data = SignatureCodec.DataToSign(request.HttpMethod, request.Url, request.Body, ts);
        var sig  = SignatureCodec.HmacSha256Hex(data, credentials.AccessSecret);

        var signed = new Dictionary<string, string>
        {
            ["X-Access-Key"] = credentials.AccessKey,
            ["X-Signature"]  = sig,
            ["X-Timestamp"]  = ts.ToString()
        };

        return await next(request.SetHeaders(signed), ct);
    }
}
