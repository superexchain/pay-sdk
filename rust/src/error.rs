use thiserror::Error;

#[derive(Debug, Error)]
pub enum NovaxError {
    #[error("transport error: {0}")]
    Transport(String),

    #[error("json error: {0}")]
    Json(#[from] serde_json::Error),

    #[error("signature error: {0}")]
    Signature(String),

    #[error("config error: {0}")]
    Config(String),
}

impl From<reqwest::Error> for NovaxError {
    fn from(e: reqwest::Error) -> Self {
        NovaxError::Transport(e.to_string())
    }
}
