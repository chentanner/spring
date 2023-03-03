package com.sss.app.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.snapshot.WarningSnapshot;

public abstract class AbstractErrorSnapshot {

    protected ErrorResponse error = new ErrorResponse();
    protected WarningSnapshot warning = new WarningSnapshot();
    protected boolean isPermissionException = false;

    public AbstractErrorSnapshot() {
    }

    public AbstractErrorSnapshot(WarningSnapshot warning) {
        this.warning = warning;
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

    public WarningSnapshot getWarning() {
        return warning;
    }

    public void setWarning(WarningSnapshot warning) {
        this.warning = warning;
    }

    public boolean hasWarnings() {
        return warning != null;
    }
}
