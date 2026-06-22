from __future__ import annotations

import dataclasses
import json
import logging
import typing
from decimal import Decimal
from typing import Any, get_args, get_origin

from novax_sdk.auth.credentials import AccessKeyCredentials
from novax_sdk.config import NovaxConfig
from novax_sdk.http.interceptor import Interceptor
from novax_sdk.http.interceptor_chain import InterceptorChain
from novax_sdk.http.interceptors.header_interceptor import HeaderInterceptor
from novax_sdk.http.interceptors.signature_interceptor import SignatureInterceptor
from novax_sdk.http.sdk_request import SdkRequest
from novax_sdk.model import ReturnResult
from novax_sdk.request.api_request import ApiRequest

_LOG = logging.getLogger(__name__)


class NovaxClient:
    """Sole entry point. Adding an endpoint never requires touching this class —
    just pass an ``ApiRequest`` subclass to ``execute()``."""

    def __init__(self, config: NovaxConfig) -> None:
        interceptors: list[Interceptor] = list(config.interceptors)
        if config.default_headers:
            interceptors.append(HeaderInterceptor(config.default_headers))
        if isinstance(config.credentials, AccessKeyCredentials):
            interceptors.append(SignatureInterceptor(config.credentials))
        self._chain = InterceptorChain(config.transport, interceptors)
        self._endpoint = config.endpoint

    def execute(self, request: ApiRequest) -> ReturnResult:
        sdk_req = SdkRequest.from_api_request(self._endpoint, request)
        resp = self._chain.proceed(sdk_req)
        raw = json.loads(resp.body)
        result = ReturnResult(
            code=raw.get("code"),
            msg=raw.get("msg"),
            data=_deserialize(raw.get("data"), request.response_type),
        )
        if not result.is_success():
            _LOG.warning(
                "business error: %s %s code=%s message=%s",
                sdk_req.method.value, sdk_req.url, result.code, result.msg,
            )
        return result

    @classmethod
    def builder(cls) -> NovaxClientBuilder:
        return NovaxClientBuilder()


class NovaxClientBuilder:
    def __init__(self) -> None:
        self._endpoint = ""
        self._credentials: AccessKeyCredentials | None = None
        self._timeout = 30.0
        self._default_headers: dict[str, str] = {}
        self._interceptors: list[Interceptor] = []
        self._verify_ssl = True

    def endpoint(self, url: str) -> NovaxClientBuilder:
        self._endpoint = url
        return self

    def access_key(self, access_key: str, access_secret: str) -> NovaxClientBuilder:
        self._credentials = AccessKeyCredentials(access_key, access_secret)
        return self

    def client_ip(self, ip: str) -> NovaxClientBuilder:
        self._default_headers["ip"] = ip
        return self

    def language(self, lang: str) -> NovaxClientBuilder:
        self._default_headers["language"] = lang
        return self

    def add_interceptor(self, interceptor: Interceptor) -> NovaxClientBuilder:
        self._interceptors.append(interceptor)
        return self

    def insecure_tls(self) -> NovaxClientBuilder:
        """DEV ONLY — disables TLS certificate validation."""
        self._verify_ssl = False
        return self

    def build(self) -> NovaxClient:
        config = NovaxConfig(
            endpoint=self._endpoint,
            credentials=self._credentials,
            timeout=self._timeout,
            default_headers=self._default_headers,
            interceptors=self._interceptors,
            verify_ssl=self._verify_ssl,
        )
        return NovaxClient(config)


# ── deserialisation ──────────────────────────────────────────────────────────

def _snake_to_camel(name: str) -> str:
    parts = name.split("_")
    return parts[0] + "".join(p.title() for p in parts[1:])


def _deserialize(data: Any, typ: Any) -> Any:
    if data is None or typ is None:
        return data

    origin = get_origin(typ)

    if origin is list:
        item_cls = get_args(typ)[0]
        return [_deserialize(item, item_cls) for item in (data or [])]

    if typ is Decimal:
        return Decimal(str(data))

    if dataclasses.is_dataclass(typ) and isinstance(data, dict):
        resolved = typing.get_type_hints(typ)
        kwargs: dict[str, Any] = {}
        for f in dataclasses.fields(typ):
            camel_key = _snake_to_camel(f.name)
            val = data.get(camel_key, data.get(f.name))
            kwargs[f.name] = _deserialize(val, resolved.get(f.name))
        return typ(**kwargs)

    return data
