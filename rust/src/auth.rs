/// Access-key + access-secret pair.
/// The server resolves `access_key` to an `ApiKey` record and injects
/// `user_id` server-side — the SDK never sends it.
#[derive(Clone)]
pub struct AccessKeyCredentials {
    pub access_key:    String,
    pub access_secret: String,
}

impl AccessKeyCredentials {
    pub fn new(access_key: impl Into<String>, access_secret: impl Into<String>) -> Self {
        Self {
            access_key:    access_key.into(),
            access_secret: access_secret.into(),
        }
    }
}
