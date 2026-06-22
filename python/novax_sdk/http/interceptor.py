from __future__ import annotations

from abc import ABC, abstractmethod

from .sdk_request import SdkRequest
from .sdk_response import SdkResponse


class Chain(ABC):
    @property
    @abstractmethod
    def request(self) -> SdkRequest:
        ...

    @abstractmethod
    def proceed(self, request: SdkRequest) -> SdkResponse:
        ...


class Interceptor(ABC):
    """Cross-cutting hook. Implementations must call chain.proceed() exactly once."""

    @abstractmethod
    def intercept(self, chain: Chain) -> SdkResponse:
        ...
