package com.sss.app.core.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorSnapshot {
    private String errorCode = null;
    private String errorMessage = null;

    private boolean isPermissionException = false;

    public ErrorSnapshot() {
    }

    public ErrorSnapshot(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @JsonIgnore
    public boolean hasError() {
        return errorCode != null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isPermissionException() {
        return isPermissionException;
    }

    public void setPermissionException(boolean permissionException) {
        isPermissionException = permissionException;
    }
}
