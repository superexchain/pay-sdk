from __future__ import annotations
from dataclasses import dataclass
from typing import Generic, Optional, TypeVar

T = TypeVar("T")


@dataclass
class ReturnResult(Generic[T]):
    code: Optional[int]
    msg: Optional[str]
    data: Optional[T]

    def is_success(self) -> bool:
        return self.code == 200
