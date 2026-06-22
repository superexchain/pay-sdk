from novax_sdk.http.interceptor import Chain, Interceptor
from novax_sdk.http.sdk_response import SdkResponse


class HeaderInterceptor(Interceptor):
    """Appends fixed headers (ip, language, …) to every request.
    Uses append_headers so per-request and default values both go on the wire."""

    def __init__(self, defaults: dict[str, str]) -> None:
        self._defaults = dict(defaults)

    def intercept(self, chain: Chain) -> SdkResponse:
        req = chain.request
        if self._defaults:
            req = req.append_headers(self._defaults)
        return chain.proceed(req)
