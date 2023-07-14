package com.sss.app.core.snapshot;

import com.sss.app.core.exception.ApplicationValidationException;

public abstract class BaseDetail<T> {
    public abstract void shallowCopyFrom(T copy);

    public abstract void validate() throws ApplicationValidationException;
}
