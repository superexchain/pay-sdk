use serde::de::DeserializeOwned;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum HttpMethod {
    Get,
    Post,
    Put,
    Patch,
    Delete,
}

impl HttpMethod {
    pub fn as_str(self) -> &'static str {
        match self {
            HttpMethod::Get    => "GET",
            HttpMethod::Post   => "POST",
            HttpMethod::Put    => "PUT",
            HttpMethod::Patch  => "PATCH",
            HttpMethod::Delete => "DELETE",
        }
    }
}

/// Self-describing API request. Implement one type per endpoint.
/// `NovaxClient::execute` is the sole entry point — adding an endpoint
/// means writing one new type; nothing else changes.
pub trait ApiRequest {
    type Response: DeserializeOwned;

    fn method(&self) -> HttpMethod;
    fn path(&self) -> &str;

    fn query_params(&self) -> Vec<(String, String)> {
        vec![]
    }

    fn extra_headers(&self) -> Vec<(String, String)> {
        vec![]
    }

    fn body(&self) -> Option<serde_json::Value> {
        None
    }
}
