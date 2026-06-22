from __future__ import annotations

from .interceptor import Chain, Interceptor
from .sdk_request import SdkRequest
from .sdk_response import SdkResponse
from .transport import HttpTransport


class InterceptorChain:
    def __init__(self, transport: HttpTransport, interceptors: list[Interceptor]) -> None:
        self._transport = transport
        self._interceptors = list(interceptors)

    def proceed(self, request: SdkRequest) -> SdkResponse:
        return _Node(request, 0, self._interceptors, self._transport).proceed(request)


class _Node(Chain):
    def __init__(self, req: SdkRequest, index: int,
                 interceptors: list[Interceptor], transport: HttpTransport) -> None:
        self._req = req
        self._index = index
        self._interceptors = interceptors
        self._transport = transport

    @property
    def request(self) -> SdkRequest:
        return self._req

    def proceed(self, request: SdkRequest) -> SdkResponse:
        if self._index < len(self._interceptors):
            return self._interceptors[self._index].intercept(
                _Node(request, self._index + 1, self._interceptors, self._transport)
            )
        return self._transport.execute(request)
