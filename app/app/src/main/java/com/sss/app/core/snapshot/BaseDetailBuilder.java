package com.sss.app.core.snapshot;

import java.util.function.Supplier;

public abstract class BaseDetailBuilder<T extends BaseDetail<T>> {

    protected T detail = constructorSupplier().get();

    protected abstract Supplier<T> constructorSupplier();

    public T build() {
        T builtDetail = constructorSupplier().get();
        builtDetail.shallowCopyFrom(this.detail);
        return builtDetail;
    }
}
