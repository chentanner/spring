package com.sss.app.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sss.app.core.enums.TransactionErrorCode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    protected String errorCode = TransactionErrorCode.SUCCESS.getCode();
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void shallowCopyFrom(ErrorResponse copy) {
        this.errorCode = copy.errorCode;
        this.errorMessage = copy.errorMessage;
    }

    @JsonIgnore
    public boolean wasSuccessful() {
        if (errorCode == null)
            return true;
        return TransactionErrorCode.SUCCESS.getCode().equals(errorCode);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}