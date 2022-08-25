package com.github.kanas.rest.domain;

import com.github.kanas.core.Line;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class Flow<T> {

    private Supplier<T> nullHandler = this::defaultNullHandler;
    private final Lines<T> lines = new Lines<>();

    private final ResponseValue responseValue;

    protected Flow(@NotNull final ResponseValue responseValue) {
        this.responseValue = responseValue;
    }

    public T get() {
        Object value = responseValue.get();
        if (value == null) return nullHandler.get();
        if (!matchType(value.getClass())) throw new FlowProducingException("Error, type is mismatch.");
        return lines.produce((T) value);
    }

    protected T defaultNullHandler() {
        return null;
    }

    public void setNullHandler(@NotNull final Supplier<T> nullHandler) {
        this.nullHandler = nullHandler;
    }

    protected void addLine(@NotNull final Line<T> line) {
        this.lines.add(line);
    }

    protected abstract boolean matchType(@NotNull Class<?> underlyingType);

}
