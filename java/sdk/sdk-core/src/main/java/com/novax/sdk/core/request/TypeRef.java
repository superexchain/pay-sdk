package com.novax.sdk.core.request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Carries the response type for an {@link ApiRequest}. Subclass anonymously to
 * capture generic parameters (super type token pattern):
 *
 * <pre>{@code
 *   TypeRef<List<Foo>> ref = new TypeRef<>() {};
 *   TypeRef<String> simple = TypeRef.of(String.class);
 * }</pre>
 */
public abstract class TypeRef<T> {

    private final Type type;

    protected TypeRef() {
        Type sup = getClass().getGenericSuperclass();
        if (!(sup instanceof ParameterizedType pt)) {
            throw new IllegalStateException(
                    "TypeRef must be parameterised — use `new TypeRef<X>() {}`");
        }
        this.type = pt.getActualTypeArguments()[0];
    }

    private TypeRef(Type type) {
        this.type = type;
    }

    public Type type() {
        return type;
    }

    public static <T> TypeRef<T> of(Class<T> raw) {
        return new TypeRef<>(raw) {
        };
    }
}
