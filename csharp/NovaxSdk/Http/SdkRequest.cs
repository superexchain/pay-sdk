using System.Text.Json;

namespace NovaxSdk.Http;

/// <summary>
/// Wire-level request. Headers support multiple values per name (RFC 7230 §3.2.2).
/// Use <see cref="AppendHeaders"/> for multi-value semantics and
/// <see cref="SetHeaders"/> for single-value replacement.
/// </summary>
public sealed record SdkRequest(
    string HttpMethod,
    string Url,
    IReadOnlyDictionary<string, IReadOnlyList<string>> Headers,
    byte[]? Body)
{
    internal static SdkRequest FromApiRequest<T>(string endpoint, IApiRequest<T> request)
    {
        var url     = BuildUrl(endpoint, request.Path, request.QueryParams);
        var headers = new Dictionary<string, List<string>>(StringComparer.OrdinalIgnoreCase);

        if (request.ExtraHeaders is not null)
            foreach (var (k, v) in request.ExtraHeaders)
                headers.GetOrCreate(k).Add(v);

        byte[]? body = null;
        if (request.Body is not null)
        {
            body = JsonSerializer.SerializeToUtf8Bytes(request.Body, NovaxClient.JsonOptions);
            headers.GetOrCreate("Content-Type").Add("application/json");
        }

        return new SdkRequest(request.HttpMethod, url, headers.Freeze(), body);
    }

    /// <summary>Appends values — both existing and new are sent (HTTP multi-value).</summary>
    public SdkRequest AppendHeaders(IReadOnlyDictionary<string, string> additional)
    {
        var headers = Headers.Mutable();
        foreach (var (k, v) in additional)
            headers.GetOrCreate(k).Add(v);
        return this with { Headers = headers.Freeze() };
    }

    /// <summary>Replaces existing values for the given keys (single-value semantics).</summary>
    public SdkRequest SetHeaders(IReadOnlyDictionary<string, string> overrides)
    {
        var headers = Headers.Mutable();
        foreach (var (k, v) in overrides)
            headers[k] = [v];
        return this with { Headers = headers.Freeze() };
    }

    private static string BuildUrl(
        string endpoint, string path,
        IReadOnlyDictionary<string, string>? queryParams)
    {
        if (!path.StartsWith('/')) path = "/" + path;
        var url = endpoint.TrimEnd('/') + path;
        if (queryParams is { Count: > 0 })
        {
            var qs = string.Join("&", queryParams.Select(
                kvp => $"{Uri.EscapeDataString(kvp.Key)}={Uri.EscapeDataString(kvp.Value)}"));
            url += "?" + qs;
        }
        return url;
    }
}

file static class HeaderExtensions
{
    public static List<string> GetOrCreate(
        this Dictionary<string, List<string>> dict, string key)
    {
        if (!dict.TryGetValue(key, out var list))
            dict[key] = list = [];
        return list;
    }

    public static IReadOnlyDictionary<string, IReadOnlyList<string>> Freeze(
        this Dictionary<string, List<string>> dict)
        => dict.ToDictionary(
            kvp => kvp.Key,
            kvp => (IReadOnlyList<string>)kvp.Value.AsReadOnly(),
            StringComparer.OrdinalIgnoreCase);

    public static Dictionary<string, List<string>> Mutable(
        this IReadOnlyDictionary<string, IReadOnlyList<string>> dict)
        => dict.ToDictionary(
            kvp => kvp.Key,
            kvp => kvp.Value.ToList(),
            StringComparer.OrdinalIgnoreCase);
}
