import time
from typing import Callable, Optional

from novax_sdk.auth.credentials import AccessKeyCredentials
from novax_sdk.http.interceptor import Chain, Interceptor
from novax_sdk.http.sdk_response import SdkResponse
from novax_sdk.http.signature_codec import SignatureCodec


class SignatureInterceptor(Interceptor):
    """Stamps X-Access-Key / X-Signature / X-Timestamp on signed paths.
    Only /api/pay/v3/* is signed; /pay/public/* passes through.
    Uses set_headers (replace) — signatures are single-valued."""

    def __init__(self, credentials: AccessKeyCredentials,
                 clock: Optional[Callable[[], int]] = None) -> None:
        self._credentials = credentials
        self._clock = clock or (lambda: int(time.time() * 1000))

    def intercept(self, chain: Chain) -> SdkResponse:
        req = chain.request
        if not self._should_sign(req.url):
            return chain.proceed(req)
        ts = self._clock()
        data = SignatureCodec.data_to_sign(req.method, req.url, req.body, ts)
        sig = SignatureCodec.hmac_sha256_hex(data, self._credentials.access_secret)
        signed = {
            "X-Access-Key": self._credentials.access_key,
            "X-Signature": sig,
            "X-Timestamp": str(ts),
        }
        return chain.proceed(req.set_headers(signed))

    @staticmethod
    def _should_sign(url: str) -> bool:
        from urllib.parse import urlparse
        path = urlparse(url).path
        return path.startswith("/api/pay/v3/")
