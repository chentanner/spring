package com.sss.app.core.auth.exception;

import com.sss.app.core.enums.TransactionErrorCode;

public class UserAuthenticationException extends Exception {
    private String errorCode;

    public UserAuthenticationException() {

    }

    public UserAuthenticationException(TransactionErrorCode errorCode) {
        super();
        this.errorCode = errorCode.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
