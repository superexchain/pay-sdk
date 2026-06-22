from __future__ import annotations

from typing import Optional

from novax_sdk.auth.credentials import AccessKeyCredentials
from novax_sdk.http.interceptor import Interceptor
from novax_sdk.http.transport import HttpTransport, RequestsTransport


class NovaxConfig:
    def __init__(
        self,
        endpoint: str,
        credentials: Optional[AccessKeyCredentials] = None,
        timeout: float = 30.0,
        default_headers: Optional[dict[str, str]] = None,
        interceptors: Optional[list[Interceptor]] = None,
        transport: Optional[HttpTransport] = None,
        verify_ssl: bool = True,
    ) -> None:
        if not endpoint:
            raise ValueError("endpoint is required")
        self.endpoint = endpoint.rstrip("/")
        self.credentials = credentials
        self.timeout = timeout
        self.default_headers: dict[str, str] = default_headers or {}
        self.interceptors: list[Interceptor] = interceptors or []
        self.transport = transport or RequestsTransport(
            timeout=timeout, verify_ssl=verify_ssl
        )
