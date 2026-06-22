import logging
import time

from novax_sdk.http.interceptor import Chain, Interceptor
from novax_sdk.http.sdk_response import SdkResponse

_LOG = logging.getLogger(__name__)
_ERROR_BODY_MAX = 2048


class LoggingInterceptor(Interceptor):
    """Logs request/response. INFO normally; ERROR on HTTP >=500 with body."""

    def intercept(self, chain: Chain) -> SdkResponse:
        req = chain.request
        _LOG.info("-> %s %s", req.method.value, req.url)
        t0 = time.monotonic()
        resp = chain.proceed(req)
        ms = int((time.monotonic() - t0) * 1000)
        if resp.status_code >= 500:
            body_str = resp.body.decode(errors="replace") if resp.body else "<empty>"
            if len(body_str) > _ERROR_BODY_MAX:
                body_str = body_str[:_ERROR_BODY_MAX] + f"…(+{len(body_str)-_ERROR_BODY_MAX} chars)"
            _LOG.error("<- %s (%dms) %s %s body=%s",
                       resp.status_code, ms, req.method.value, req.url, body_str)
        else:
            _LOG.info("<- %s (%dms)", resp.status_code, ms)
        return resp
