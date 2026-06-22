from __future__ import annotations

from abc import ABC, abstractmethod
from enum import Enum
from typing import Any, Optional, Type


class HttpMethod(Enum):
    GET = "GET"
    POST = "POST"
    PUT = "PUT"
    PATCH = "PATCH"
    DELETE = "DELETE"


class ApiRequest(ABC):
    """Self-describing request. Subclass one per endpoint; NovaxClient.execute()
    is the only entry point — nothing else needs to change when adding endpoints."""

    @property
    @abstractmethod
    def method(self) -> HttpMethod:
        ...

    @property
    @abstractmethod
    def path(self) -> str:
        ...

    def query_params(self) -> dict[str, Any]:
        return {}

    def headers(self) -> dict[str, str]:
        return {}

    def body(self) -> Optional[Any]:
        return None

    @property
    @abstractmethod
    def response_type(self) -> Any:
        """Return the type to deserialise ReturnResult.data into.
        Use a class for objects, list[X] for lists, str/int/bool for primitives."""
        ...
