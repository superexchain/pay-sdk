from __future__ import annotations

from abc import ABC, abstractmethod

import requests as req_lib

from .sdk_request import SdkRequest
from .sdk_response import SdkResponse
from novax_sdk.exceptions import NovaxTransportException


class HttpTransport(ABC):
    @abstractmethod
    def execute(self, request: SdkRequest) -> SdkResponse:
        ...


class RequestsTransport(HttpTransport):
    """Default transport backed by the `requests` library."""

    def __init__(self, timeout: float = 30.0, verify_ssl: bool = True) -> None:
        self._timeout = timeout
        self._verify_ssl = verify_ssl
        self._session = req_lib.Session()

    def execute(self, request: SdkRequest) -> SdkResponse:
        try:
            resp = self._session.request(
                method=request.method.value,
                url=request.url,
                headers=dict(request.flat_headers()),
                data=request.body,
                timeout=self._timeout,
                verify=self._verify_ssl,
            )
            headers: dict[str, list[str]] = {}
            for k, v in resp.headers.items():
                headers.setdefault(k, []).append(v)
            return SdkResponse(
                status_code=resp.status_code,
                headers=headers,
                body=resp.content,
            )
        except req_lib.RequestException as e:
            raise NovaxTransportException(f"HTTP request failed: {request.url}") from e
