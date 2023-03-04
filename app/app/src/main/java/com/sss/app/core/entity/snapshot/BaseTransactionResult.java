package com.sss.app.core.entity.snapshot;

public abstract class BaseTransactionResult {

    private ErrorResponse error;

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public boolean wasSuccessful() {
        return error == null || error.wasSuccessful();
    }
}
