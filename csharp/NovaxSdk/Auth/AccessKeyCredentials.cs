namespace NovaxSdk.Auth;

/// <summary>
/// Access-key + access-secret pair.
/// The server resolves AccessKey to an ApiKey record and injects
/// UserId server-side — the SDK never sends it.
/// </summary>
public sealed record AccessKeyCredentials(string AccessKey, string AccessSecret);
