class AccessKeyCredentials:
    """Access-key + access-secret pair. The server resolves accessKey to an
    ApiKey record and injects userId server-side — the SDK never sends it."""

    def __init__(self, access_key: str, access_secret: str) -> None:
        if not access_key:
            raise ValueError("access_key is required")
        if not access_secret:
            raise ValueError("access_secret is required")
        self.access_key = access_key
        self.access_secret = access_secret
