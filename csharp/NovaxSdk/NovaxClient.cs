using System.Text.Json;
using System.Text.Json.Serialization;
using NovaxSdk.Auth;
using NovaxSdk.Exceptions;
using NovaxSdk.Http;
using NovaxSdk.Http.Interceptors;
using NovaxSdk.Pay;

namespace NovaxSdk;

/// <summary>
/// Sole entry point for all API calls. Create with <see cref="Builder"/>.
/// Safe for concurrent use.
///
/// <code>
/// var client = NovaxClient.Builder()
///     .WithEndpoint("https://api.novax.dev/api")
///     .WithAccessKey("key", "secret")
///     .Build();
///
/// var resp = await client.Pay.TokenAsync(new PayTokenRequest("orderId", 1m));
/// </code>
/// </summary>
public sealed class NovaxClient
{
    internal static readonly JsonSerializerOptions JsonOptions = new()
    {
        PropertyNamingPolicy       = JsonNamingPolicy.CamelCase,
        PropertyNameCaseInsensitive = true,
        DefaultIgnoreCondition     = JsonIgnoreCondition.WhenWritingNull
    };

    private readonly string _endpoint;
    private readonly InterceptorChain _chain;

    /// <summary>Pay API methods.</summary>
    public PayClient Pay { get; }

    private NovaxClient(NovaxConfig config)
    {
        _endpoint = config.Endpoint;

        var interceptors = new List<IInterceptor>(config.Interceptors);
        if (config.DefaultHeaders.Count > 0)
            interceptors.Add(new HeaderInterceptor(config.DefaultHeaders));
        if (config.Credentials is not null)
            interceptors.Add(new SignatureInterceptor(config.Credentials));

        _chain = new InterceptorChain(config.Transport, interceptors.AsReadOnly());
        Pay    = new PayClient(this);
    }

    public static NovaxClientBuilder Builder() => new();

    internal async Task<ReturnResult<T>> ExecuteAsync<T>(
        IApiRequest<T> request,
        CancellationToken cancellationToken = default)
    {
        var sdkRequest  = SdkRequest.FromApiRequest(_endpoint, request);
        var sdkResponse = await _chain.ProceedAsync(sdkRequest, cancellationToken);

        var result = JsonSerializer.Deserialize<ReturnResult<T>>(sdkResponse.Body, JsonOptions)
            ?? throw new NovaxException("Failed to deserialize response.");

        if (!result.IsSuccess)
        {
            System.Diagnostics.Debug.WriteLine(
                $"[NovaxSdk] business error: {request.HttpMethod} {request.Path} " +
                $"code={result.Code} msg={result.Msg}");
        }

        return result;
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public sealed class NovaxClientBuilder
    {
        private string? _endpoint;
        private AccessKeyCredentials? _credentials;
        private TimeSpan _timeout = TimeSpan.FromSeconds(30);
        private readonly Dictionary<string, string> _defaultHeaders = new();
        private readonly List<IInterceptor> _interceptors = new();
        private bool _insecureTls;
        private IHttpTransport? _transport;

        public NovaxClientBuilder WithEndpoint(string endpoint)
        {
            _endpoint = endpoint;
            return this;
        }

        public NovaxClientBuilder WithAccessKey(string key, string secret)
        {
            _credentials = new AccessKeyCredentials(key, secret);
            return this;
        }

        /// <summary>Sent as the <c>ip</c> header for server-side IP whitelist checks.</summary>
        public NovaxClientBuilder WithClientIp(string ip)
        {
            _defaultHeaders["ip"] = ip;
            return this;
        }

        public NovaxClientBuilder WithLanguage(string language)
        {
            _defaultHeaders["language"] = language;
            return this;
        }

        public NovaxClientBuilder WithTimeout(TimeSpan timeout)
        {
            _timeout = timeout;
            return this;
        }

        public NovaxClientBuilder AddInterceptor(IInterceptor interceptor)
        {
            _interceptors.Add(interceptor);
            return this;
        }

        /// <summary>DEV ONLY — disables TLS certificate validation.</summary>
        public NovaxClientBuilder WithInsecureTls()
        {
            _insecureTls = true;
            return this;
        }

        /// <summary>Replaces the default transport. Useful for unit testing.</summary>
        public NovaxClientBuilder WithTransport(IHttpTransport transport)
        {
            _transport = transport;
            return this;
        }

        public NovaxClient Build()
        {
            if (string.IsNullOrWhiteSpace(_endpoint))
                throw new ArgumentException("Endpoint is required.");

            var transport = _transport
                ?? new DefaultHttpTransport(_timeout, _insecureTls);

            var config = new NovaxConfig(
                Endpoint:       _endpoint.TrimEnd('/'),
                Credentials:    _credentials,
                Timeout:        _timeout,
                DefaultHeaders: _defaultHeaders.AsReadOnly(),
                Interceptors:   _interceptors.AsReadOnly(),
                Transport:      transport);

            return new NovaxClient(config);
        }
    }
}

// internal record to pass config around
internal sealed record NovaxConfig(
    string Endpoint,
    AccessKeyCredentials? Credentials,
    TimeSpan Timeout,
    IReadOnlyDictionary<string, string> DefaultHeaders,
    IReadOnlyList<IInterceptor> Interceptors,
    IHttpTransport Transport);
