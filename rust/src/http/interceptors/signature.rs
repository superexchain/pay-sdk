use std::time::{SystemTime, UNIX_EPOCH};

use crate::auth::AccessKeyCredentials;
use crate::error::NovaxError;
use crate::http::interceptor::{Chain, Interceptor};
use crate::http::signature_codec::SignatureCodec;
use crate::http::{SdkRequest, SdkResponse};

/// Stamps `X-Access-Key` / `X-Signature` / `X-Timestamp` on signed paths.
/// Only `/api/pay/v3/*` is signed; `/pay/public/*` passes through.
pub struct SignatureInterceptor {
    credentials: AccessKeyCredentials,
}

impl SignatureInterceptor {
    pub fn new(credentials: AccessKeyCredentials) -> Self {
        Self { credentials }
    }
}

impl Interceptor for SignatureInterceptor {
    fn intercept(&self, request: SdkRequest, chain: &dyn Chain) -> Result<SdkResponse, NovaxError> {
        if !should_sign(&request.url) {
            return chain.proceed(request);
        }

        let ts = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .expect("time went backwards")
            .as_millis() as u64;

        let data = SignatureCodec::data_to_sign(
            request.method,
            &request.url,
            request.body.as_deref(),
            ts,
        )?;
        let sig = SignatureCodec::hmac_sha256_hex(&data, &self.credentials.access_secret)?;

        chain.proceed(request.set_headers([
            ("X-Access-Key".into(), self.credentials.access_key.clone()),
            ("X-Signature".into(),  sig),
            ("X-Timestamp".into(),  ts.to_string()),
        ]))
    }
}

fn should_sign(url: &str) -> bool {
    url.contains("/api/pay/v3/")
}
