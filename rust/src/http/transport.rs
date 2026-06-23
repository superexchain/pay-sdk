use std::time::Duration;

use reqwest::blocking::ClientBuilder;
use reqwest::header::{HeaderMap, HeaderName, HeaderValue};

use crate::error::NovaxError;
use super::{SdkRequest, SdkResponse};

pub trait Transport: Send + Sync {
    fn execute(&self, request: SdkRequest) -> Result<SdkResponse, NovaxError>;
}

/// Default transport backed by `reqwest::blocking`.
pub struct ReqwestTransport {
    client: reqwest::blocking::Client,
}

impl ReqwestTransport {
    pub fn new(timeout: Duration, verify_ssl: bool) -> Self {
        let client = ClientBuilder::new()
            .timeout(timeout)
            .danger_accept_invalid_certs(!verify_ssl)
            .build()
            .expect("failed to build reqwest client");
        Self { client }
    }
}

impl Transport for ReqwestTransport {
    fn execute(&self, request: SdkRequest) -> Result<SdkResponse, NovaxError> {
        let method = reqwest::Method::from_bytes(request.method.as_str().as_bytes())
            .unwrap_or(reqwest::Method::GET);

        let mut header_map = HeaderMap::new();
        for (k, v) in &request.headers {
            if let (Ok(name), Ok(value)) = (k.parse::<HeaderName>(), HeaderValue::from_str(v)) {
                header_map.append(name, value);
            }
        }

        let mut builder = self.client.request(method, &request.url).headers(header_map);
        if let Some(body) = request.body {
            builder = builder.body(body);
        }

        let resp    = builder.send()?;
        let status  = resp.status().as_u16();
        let headers = resp
            .headers()
            .iter()
            .map(|(k, v)| (k.as_str().to_owned(), v.to_str().unwrap_or("").to_owned()))
            .collect();
        let body = resp.bytes()?.to_vec();

        Ok(SdkResponse { status_code: status, headers, body })
    }
}
