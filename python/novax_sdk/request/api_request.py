from abc import ABC, abstractmethod
from enum import Enum
from typing import Any


class HttpMethod(Enum):
    GET = "GET"
    POST = "POST"
    PUT = "PUT"
    PATCH = "PATCH"
    DELETE = "DELETE"


class ApiRequest(ABC):
    """Base class for every API endpoint.

    Subclass with ``@dataclass`` and override the abstract properties/methods
    you need. ``query_params``, ``headers``, and ``body`` have empty defaults.
    """

    @property
    @abstractmethod
    def method(self) -> HttpMethod: ...

    @property
    @abstractmethod
    def path(self) -> str: ...

    def query_params(self) -> dict[str, Any]:
        return {}

    def headers(self) -> dict[str, str]:
        return {}

    def body(self) -> Any:
        return None

    @property
    @abstractmethod
    def response_type(self) -> Any:
        """Type to deserialise ``ReturnResult.data`` into.
        Use a dataclass, ``list[X]``, ``str``, ``bool``, etc., or ``None``."""
        ...
