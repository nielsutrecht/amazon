package com.nibado.amazon.tool.cli.command;

import lombok.Getter;

@Getter
public class Result<T> {
    private final T value;
    private final boolean success;

    public Result(T value) {
        this.value = value;
        this.success = true;
    }

    private Result(boolean success) {
        this.value = null;
        this.success = success;
    }

    public Result() {
        this.value = null;
        this.success = true;
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    public boolean is(final Class<?> clazz) {

        return !isEmpty() && clazz.isInstance(value);
    }

    public <X> X as(final Class<X> clazz) {
        return (X) value;
    }

    public static <T> Result<T> fail() {
        return new Result<>(false);
    }
}
