use serde::Deserialize;

/// Mirror of the server-side response envelope.
/// `code == 200` is treated as success.
#[derive(Debug, Deserialize)]
pub struct ReturnResult<T> {
    pub code: Option<i32>,
    pub msg:  Option<String>,
    pub data: Option<T>,
}

impl<T> ReturnResult<T> {
    pub fn is_success(&self) -> bool {
        self.code == Some(200)
    }
}
