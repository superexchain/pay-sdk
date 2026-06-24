namespace NovaxSdk.Exceptions;

public class NovaxException(string message, Exception? inner = null)
    : Exception(message, inner);

public class NovaxTransportException(string message, Exception? inner = null)
    : NovaxException(message, inner);
