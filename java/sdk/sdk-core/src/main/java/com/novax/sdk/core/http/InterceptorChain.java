package com.novax.sdk.core.http;

import java.io.IOException;
import java.util.List;

/** Drives the interceptor chain and hands the final request to the transport. */
public final class InterceptorChain {

    private final HttpTransport transport;
    private final List<Interceptor> interceptors;

    public InterceptorChain(HttpTransport transport, List<Interceptor> interceptors) {
        this.transport = transport;
        this.interceptors = List.copyOf(interceptors);
    }

    public SdkResponse proceed(SdkRequest request) throws IOException {
        return new Node(request, 0).proceed(request);
    }

    private final class Node implements Interceptor.Chain {

        private final SdkRequest request;
        private final int index;

        Node(SdkRequest request, int index) {
            this.request = request;
            this.index = index;
        }

        @Override
        public SdkRequest request() {
            return request;
        }

        @Override
        public SdkResponse proceed(SdkRequest req) throws IOException {
            if (index < interceptors.size()) {
                return interceptors.get(index).intercept(new Node(req, index + 1));
            }
            return transport.execute(req);
        }
    }
}
