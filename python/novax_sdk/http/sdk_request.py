from __future__ import annotations

import json
from dataclasses import dataclass, field
from typing import Any, Optional
from urllib.parse import urlencode, quote

from novax_sdk.request.api_request import ApiRequest, HttpMethod


@dataclass
class SdkRequest:
    """Wire-level request. headers is dict[str, list[str]] — HTTP allows the
    same name multiple times (RFC 7230). Use append_headers for multi-value
    semantics and set_headers for single-value replacement."""

    method: HttpMethod
    url: str
    headers: dict[str, list[str]]
    body: Optional[bytes]

    @staticmethod
    def from_api_request(endpoint: str, req: ApiRequest) -> "SdkRequest":
        url = _build_url(endpoint, req.path, req.query_params())
        headers: dict[str, list[str]] = {}
        for k, v in req.headers().items():
            headers.setdefault(k, []).append(v)
        body = None
        if req.body() is not None:
            body = json.dumps(req.body(), ensure_ascii=False).encode()
            headers.setdefault("Content-Type", []).append("application/json")
        return SdkRequest(method=req.method, url=url, headers=headers, body=body)

    def append_headers(self, extra: dict[str, str]) -> "SdkRequest":
        merged = {k: list(v) for k, v in self.headers.items()}
        for k, v in extra.items():
            merged.setdefault(k, []).append(v)
        return SdkRequest(self.method, self.url, merged, self.body)

    def set_headers(self, overrides: dict[str, str]) -> "SdkRequest":
        merged = {k: list(v) for k, v in self.headers.items()}
        for k, v in overrides.items():
            merged[k] = [v]
        return SdkRequest(self.method, self.url, merged, self.body)

    def flat_headers(self) -> list[tuple[str, str]]:
        """Flatten to (name, value) pairs for requests library."""
        return [(k, v) for k, vs in self.headers.items() for v in vs]


def _build_url(endpoint: str, path: str, query: dict[str, Any]) -> str:
    base = endpoint.rstrip("/")
    if not path.startswith("/"):
        path = "/" + path
    url = base + path
    params = {k: v for k, v in query.items() if v is not None}
    if params:
        url += "?" + urlencode(params)
    return url
