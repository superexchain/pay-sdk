use std::sync::OnceLock;

use novax_sdk::{NovaxClient, http::interceptors::LoggingInterceptor};

pub const ACCESS_KEY: &str =
    "FEafqZrMaphQ3rgsqaLEXT_PZ7r_qZddGLRqXzR3XSw7NB-EVmb41jVdy9gVTwVD";
pub const ACCESS_SECRET: &str =
    "oiAKpoyvTJ24NOL38cUtQNutBAqVeF0oH5J-AZf7_cWz3E6-wFgMuVNt7JDtF9r2";

static CLIENT: OnceLock<NovaxClient> = OnceLock::new();

pub fn client() -> &'static NovaxClient {
    CLIENT.get_or_init(|| {
        let _ = env_logger::builder().is_test(true).try_init();
        NovaxClient::builder()
            .endpoint("https://api.novax.dev/api")
            .access_key(ACCESS_KEY, ACCESS_SECRET)
            .client_ip("1.2.3.4")
            .language("zh-CN")
            .add_interceptor(LoggingInterceptor)
            .insecure_tls()
            .build()
            .expect("failed to build NovaxClient")
    })
}
