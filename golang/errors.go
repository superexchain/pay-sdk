package novax

import "fmt"

// TransportError wraps a network-level failure.
type TransportError struct{ Cause error }

func (e *TransportError) Error() string { return fmt.Sprintf("transport error: %v", e.Cause) }
func (e *TransportError) Unwrap() error { return e.Cause }

// JSONError wraps a JSON marshal/unmarshal failure.
type JSONError struct{ Cause error }

func (e *JSONError) Error() string { return fmt.Sprintf("json error: %v", e.Cause) }
func (e *JSONError) Unwrap() error { return e.Cause }
