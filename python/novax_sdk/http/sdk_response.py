from dataclasses import dataclass


@dataclass(frozen=True)
class SdkResponse:
    status_code: int
    headers: dict[str, list[str]]
    body: bytes
