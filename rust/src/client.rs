use std::sync::Arc;
use std::time::Duration;

use crate::auth::AccessKeyCredentials;
use crate::error::NovaxError;
use crate::http::interceptor::{Chain, ChainNode, Interceptor};
use crate::http::interceptors::{HeaderInterceptor, SignatureInterceptor};
use crate::http::transport::{ReqwestTransport, Transport};
use crate::http::SdkRequest;
use crate::model::ReturnResult;
use crate::request::ApiRequest;

/// Sole entry point. Adding an endpoint never requires touching this struct —
/// just call `execute` with any `ApiRequest` implementation.
pub struct NovaxClient {
    endpoint:     String,
    interceptors: Arc<Vec<Arc<dyn Interceptor>>>,
    transport:    Arc<dyn Transport>,
}

impl NovaxClient {
    pub fn builder() -> NovaxClientBuilder {
        NovaxClientBuilder::default()
    }

    pub fn execute<R>(&self, request: &impl ApiRequest<Response = R>) -> Result<ReturnResult<R>, NovaxError>
    where
        R: serde::de::DeserializeOwned,
    {
        let sdk_req  = SdkRequest::from_api_request(&self.endpoint, request)?;
        let chain    = ChainNode::new(self.interceptors.clone(), self.transport.clone());
        let sdk_resp = chain.proceed(sdk_req)?;

        let result: ReturnResult<R> = serde_json::from_slice(&sdk_resp.body)?;

        if !result.is_success() {
            log::warn!(
                "business error: {} {} code={:?} msg={:?}",
                request.method().as_str(),
                request.path(),
                result.code,
                result.msg,
            );
        }
        Ok(result)
    }
}

// ── Builder ───────────────────────────────────────────────────────────────────

#[derive(Default)]
pub struct NovaxClientBuilder {
    endpoint:        Option<String>,
    credentials:     Option<AccessKeyCredentials>,
    timeout:         Option<Duration>,
    default_headers: Vec<(String, String)>,
    interceptors:    Vec<Arc<dyn Interceptor>>,
    verify_ssl:      bool,
}

impl NovaxClientBuilder {
    pub fn endpoint(mut self, ep: impl Into<String>) -> Self {
        self.endpoint = Some(ep.into());
        self
    }

    pub fn access_key(mut self, key: impl Into<String>, secret: impl Into<String>) -> Self {
        self.credentials = Some(AccessKeyCredentials::new(key, secret));
        self
    }

    /// Sent as the `ip` header — used for server-side IP whitelist checks.
    pub fn client_ip(mut self, ip: impl Into<String>) -> Self {
        self.default_headers.push(("ip".into(), ip.into()));
        self
    }

    pub fn language(mut self, lang: impl Into<String>) -> Self {
        self.default_headers.push(("language".into(), lang.into()));
        self
    }

    pub fn add_interceptor(mut self, interceptor: impl Interceptor + 'static) -> Self {
        self.interceptors.push(Arc::new(interceptor));
        self
    }

    pub fn timeout(mut self, t: Duration) -> Self {
        self.timeout = Some(t);
        self
    }

    /// DEV ONLY — disables TLS certificate validation.
    pub fn insecure_tls(mut self) -> Self {
        self.verify_ssl = false;
        self
    }

    pub fn build(self) -> Result<NovaxClient, NovaxError> {
        let endpoint = self
            .endpoint
            .ok_or_else(|| NovaxError::Config("endpoint is required".into()))?;

        let timeout   = self.timeout.unwrap_or(Duration::from_secs(30));
        let transport = Arc::new(ReqwestTransport::new(timeout, self.verify_ssl));

        let mut interceptors = self.interceptors;
        if !self.default_headers.is_empty() {
            interceptors.push(Arc::new(HeaderInterceptor::new(self.default_headers)));
        }
        if let Some(creds) = self.credentials {
            interceptors.push(Arc::new(SignatureInterceptor::new(creds)));
        }

        Ok(NovaxClient {
            endpoint,
            interceptors: Arc::new(interceptors),
            transport,
        })
    }
}
