from dataclasses import dataclass
from typing import Generic, TypeVar

T = TypeVar("T")


@dataclass
class ReturnResult(Generic[T]):
    code: int | None
    msg: str | None
    data: T | None

    def is_success(self) -> bool:
        return self.code == 200
