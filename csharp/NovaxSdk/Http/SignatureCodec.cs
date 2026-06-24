using System.Security.Cryptography;
using System.Text;
using System.Text.Json;

namespace NovaxSdk.Http;

internal static class SignatureCodec
{
    /// <summary>
    /// Builds the canonical data-to-sign string mirroring server-side SignatureUtil:
    /// METHOD[&amp;sorted-query][&amp;sorted-body]&amp;timestamp=&lt;ms&gt;
    /// </summary>
    public static string DataToSign(
        string method, string url, byte[]? body, long timestampMs)
    {
        var parts = new List<string> { method.ToUpperInvariant() };

        var sortedQuery = SortedQuery(url);
        if (!string.IsNullOrEmpty(sortedQuery)) parts.Add(sortedQuery);

        if (body is { Length: > 0 })
        {
            var sortedBody = SortedBody(body);
            if (!string.IsNullOrEmpty(sortedBody)) parts.Add(sortedBody);
        }

        parts.Add($"timestamp={timestampMs}");
        return string.Join("&", parts);
    }

    public static string HmacSha256Hex(string data, string secret)
    {
        var key = Encoding.UTF8.GetBytes(secret);
        var hash = HMACSHA256.HashData(key, Encoding.UTF8.GetBytes(data));
        return Convert.ToHexString(hash).ToLowerInvariant();
    }

    public static bool ShouldSign(string path)
        => path.StartsWith("/api/pay/v3/", StringComparison.Ordinal);

    // ── private ───────────────────────────────────────────────────────────────

    private static string SortedQuery(string url)
    {
        var q = url.IndexOf('?');
        if (q < 0) return string.Empty;

        var grouped = new SortedDictionary<string, List<string>>(StringComparer.Ordinal);
        foreach (var pair in url[(q + 1)..].Split('&', StringSplitOptions.RemoveEmptyEntries))
        {
            var eq = pair.IndexOf('=');
            var key   = Uri.UnescapeDataString(eq >= 0 ? pair[..eq] : pair);
            var value = eq >= 0 ? Uri.UnescapeDataString(pair[(eq + 1)..]) : string.Empty;

            if (!grouped.TryGetValue(key, out var list))
                grouped[key] = list = new();
            list.Add(value);
        }

        return string.Join("&", grouped.Select(
            kvp => $"{kvp.Key}={string.Join(",", kvp.Value)}"));
    }

    private static string SortedBody(byte[] body)
    {
        try
        {
            using var doc = JsonDocument.Parse(body);
            return doc.RootElement.ValueKind == JsonValueKind.Array
                ? string.Join("&", doc.RootElement.EnumerateArray().Select(SortObject))
                : SortObject(doc.RootElement);
        }
        catch { return string.Empty; }
    }

    private static string SortObject(JsonElement element)
    {
        if (element.ValueKind != JsonValueKind.Object)
            return element.GetRawText();

        return string.Join("&", element.EnumerateObject()
            .OrderBy(p => p.Name, StringComparer.Ordinal)
            .Select(p => $"{p.Name}={RenderValue(p.Value)}"));
    }

    private static string RenderValue(JsonElement el) => el.ValueKind switch
    {
        JsonValueKind.Null   => string.Empty,
        JsonValueKind.String => el.GetString() ?? string.Empty,
        JsonValueKind.True   => "true",
        JsonValueKind.False  => "false",
        JsonValueKind.Number =>
            el.TryGetInt64(out var i) ? i.ToString()
            : el.GetDecimal().ToString("G"),
        _                    => el.GetRawText()
    };
}
