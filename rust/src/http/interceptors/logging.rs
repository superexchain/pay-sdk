use std::time::Instant;

use crate::error::NovaxError;
use crate::http::interceptor::{Chain, Interceptor};
use crate::http::{SdkRequest, SdkResponse};

const ERROR_BODY_MAX: usize = 2048;

/// Logs outbound requests and inbound responses.
/// Normal responses → `log::info!`. HTTP ≥500 → `log::error!` with body.
pub struct LoggingInterceptor;

impl Interceptor for LoggingInterceptor {
    fn intercept(&self, request: SdkRequest, chain: &dyn Chain) -> Result<SdkResponse, NovaxError> {
        log::info!("-> {} {}", request.method.as_str(), request.url);
        let t0   = Instant::now();
        let resp = chain.proceed(request)?;
        let ms   = t0.elapsed().as_millis();

        if resp.status_code >= 500 {
            let body = String::from_utf8_lossy(&resp.body);
            let body = if body.len() > ERROR_BODY_MAX {
                format!("{}…(+{} chars)", &body[..ERROR_BODY_MAX], body.len() - ERROR_BODY_MAX)
            } else {
                body.into_owned()
            };
            log::error!("<- {} ({}ms) body={}", resp.status_code, ms, body);
        } else {
            log::info!("<- {} ({}ms)", resp.status_code, ms);
        }
        Ok(resp)
    }
}
