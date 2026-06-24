namespace NovaxSdk.Http;

/// <summary>Shared helper for building optional query-param dictionaries.</summary>
internal static class QueryHelper
{
    public static IReadOnlyDictionary<string, string>? Optional(
        params (string Key, string? Value)[] pairs)
    {
        var d = pairs
            .Where(p => p.Value is not null)
            .ToDictionary(p => p.Key, p => p.Value!);
        return d.Count > 0 ? d : null;
    }
}
