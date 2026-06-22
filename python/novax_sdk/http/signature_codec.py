from __future__ import annotations

import hashlib
import hmac
import json
from typing import Optional
from urllib.parse import urlparse, parse_qs, unquote

from novax_sdk.request.api_request import HttpMethod


class SignatureCodec:
    """Mirrors the server-side SignatureUtil.

    dataToSign = METHOD[&sorted-query][&sorted-body]&timestamp=<ms>

    Query: keys decoded then sorted alphabetically; multi-values joined with ','.
    Body: top-level keys sorted; primitives as str(), nested objects/arrays as
    compact JSON — matches server-side getSortedBodyString logic.
    """

    @staticmethod
    def data_to_sign(method: HttpMethod, url: str,
                     body: Optional[bytes], timestamp_ms: int) -> str:
        parts = [method.value.upper()]

        sorted_query = SignatureCodec._sorted_query(url)
        if sorted_query:
            parts.append(sorted_query)

        if body:
            sorted_body = SignatureCodec._sorted_body(body)
            if sorted_body:
                parts.append(sorted_body)

        parts.append(f"timestamp={timestamp_ms}")
        return "&".join(parts)

    @staticmethod
    def hmac_sha256_hex(data: str, secret: str) -> str:
        return hmac.new(
            secret.encode(), data.encode(), hashlib.sha256
        ).hexdigest()

    @staticmethod
    def _sorted_query(url: str) -> str:
        parsed = urlparse(url)
        raw = parsed.query
        if not raw:
            return ""
        grouped: dict[str, list[str]] = {}
        for pair in raw.split("&"):
            if not pair:
                continue
            eq = pair.find("=")
            key = unquote(pair[:eq]) if eq >= 0 else unquote(pair)
            value = unquote(pair[eq + 1:]) if eq >= 0 else ""
            grouped.setdefault(key, []).append(value)
        return "&".join(
            f"{k}={','.join(vs)}" for k, vs in sorted(grouped.items())
        )

    @staticmethod
    def _sorted_body(body: bytes) -> str:
        try:
            parsed = json.loads(body)
        except (json.JSONDecodeError, ValueError):
            return ""
        if isinstance(parsed, list):
            parts = []
            for item in parsed:
                parts.append(SignatureCodec._sort_object(item))
            return "&".join(parts)
        return SignatureCodec._sort_object(parsed)

    @staticmethod
    def _sort_object(obj: object) -> str:
        if not isinstance(obj, dict):
            return str(obj)
        pairs = []
        for k in sorted(obj.keys()):
            v = obj[k]
            if v is None:
                rendered = ""
            elif isinstance(v, (dict, list)):
                rendered = json.dumps(v, separators=(",", ":"), ensure_ascii=False)
            elif isinstance(v, bool):
                rendered = str(v).lower()
            else:
                rendered = str(v)
            pairs.append(f"{k}={rendered}")
        return "&".join(pairs)
