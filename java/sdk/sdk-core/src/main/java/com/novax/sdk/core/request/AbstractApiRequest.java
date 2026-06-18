package com.novax.sdk.core.request;

/**
 * Convenience base class — picks up the default {@code queryParams/headers/body}
 * implementations from {@link ApiRequest}. Concrete requests only override what
 * they actually need.
 */
public abstract class AbstractApiRequest<R> implements ApiRequest<R> {
}
