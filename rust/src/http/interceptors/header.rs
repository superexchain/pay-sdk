use crate::error::NovaxError;
use crate::http::interceptor::{Chain, Interceptor};
use crate::http::{SdkRequest, SdkResponse};

pub struct HeaderInterceptor {
    defaults: Vec<(String, String)>,
}

impl HeaderInterceptor {
    pub fn new(defaults: impl IntoIterator<Item = (String, String)>) -> Self {
        Self { defaults: defaults.into_iter().collect() }
    }
}

impl Interceptor for HeaderInterceptor {
    fn intercept(&self, request: SdkRequest, chain: &dyn Chain) -> Result<SdkResponse, NovaxError> {
        chain.proceed(request.append_headers(self.defaults.clone()))
    }
}
