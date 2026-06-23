pub mod interceptor;
pub mod interceptors;
pub mod signature_codec;
pub mod transport;

use crate::error::NovaxError;
use crate::request::{ApiRequest, HttpMethod};

// ── Wire-level request ────────────────────────────────────────────────────────

#[derive(Debug, Clone)]
pub struct SdkRequest {
    pub method:  HttpMethod,
    pub url:     String,
    /// Multi-value headers (RFC 7230 §3.2.2).
    pub headers: Vec<(String, String)>,
    pub body:    Option<Vec<u8>>,
}

impl SdkRequest {
    pub fn from_api_request<R>(
        endpoint: &str,
        request:  &impl ApiRequest<Response = R>,
    ) -> Result<Self, NovaxError>
    where
        R: serde::de::DeserializeOwned,
    {
        let url     = build_url(endpoint, request.path(), &request.query_params());
        let mut headers = request.extra_headers();
        let body    = match request.body() {
            Some(json) => {
                headers.push(("Content-Type".into(), "application/json".into()));
                Some(serde_json::to_vec(&json)?)
            }
            None => None,
        };
        Ok(SdkRequest { method: request.method(), url, headers, body })
    }

    /// Append values — both old and new are sent (HTTP multi-value semantics).
    pub fn append_headers(mut self, extra: impl IntoIterator<Item = (String, String)>) -> Self {
        self.headers.extend(extra);
        self
    }

    /// Replace all existing values for the given keys (single-value semantics).
    pub fn set_headers(mut self, overrides: impl IntoIterator<Item = (String, String)>) -> Self {
        for (k, v) in overrides {
            self.headers.retain(|(ek, _)| ek != &k);
            self.headers.push((k, v));
        }
        self
    }
}

// ── Wire-level response ───────────────────────────────────────────────────────

#[derive(Debug)]
pub struct SdkResponse {
    pub status_code: u16,
    pub headers:     Vec<(String, String)>,
    pub body:        Vec<u8>,
}

// ── URL builder ───────────────────────────────────────────────────────────────

fn build_url(endpoint: &str, path: &str, params: &[(String, String)]) -> String {
    let base = endpoint.trim_end_matches('/');
    let sep  = if path.starts_with('/') { "" } else { "/" };
    let mut url = format!("{}{}{}", base, sep, path);

    let pairs: Vec<String> = params
        .iter()
        .map(|(k, v)| format!("{}={}", urlencoding::encode(k), urlencoding::encode(v)))
        .collect();

    if !pairs.is_empty() {
        url.push('?');
        url.push_str(&pairs.join("&"));
    }
    url
}
