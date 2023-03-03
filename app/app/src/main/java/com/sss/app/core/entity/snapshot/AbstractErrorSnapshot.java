package com.sss.app.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractErrorSnapshot {

    protected ErrorResponse error = new ErrorResponse();
    protected boolean isPermissionException = false;

    public AbstractErrorSnapshot() {
    }

    public AbstractErrorSnapshot(ErrorResponse error) {
        this.error = error;
    }

    public void shallowCopyFrom(AbstractErrorSnapshot copy) {
        this.error.shallowCopyFrom(copy.error);
        this.isPermissionException = copy.isPermissionException;
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        if (error == null)
            return true;
        return error.wasSuccessful();
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public boolean isPermissionException() {
        return isPermissionException;
    }

    public void setPermissionException(boolean permissionException) {
        isPermissionException = permissionException;
    }
}
