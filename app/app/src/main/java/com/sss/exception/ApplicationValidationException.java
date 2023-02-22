package com.sss.exception;

import com.sss.enums.TransactionErrorCode;

public class ApplicationValidationException extends Exception {

    private String parms = "";
    private String errorCode;


    public ApplicationValidationException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }


    public ApplicationValidationException(TransactionErrorCode error) {
        this.errorCode = error.getCode();
    }


    public ApplicationValidationException(TransactionErrorCode error, String parms) {
        this.errorCode = error.getCode();
        this.parms = parms;
    }


    public ApplicationValidationException(String errorCode, String parms) {
        super();
        this.parms = parms;
        this.errorCode = errorCode;
    }

    public ApplicationValidationException(String errorCode, RuntimeException t) {
        super(errorCode, t);
        this.errorCode = errorCode;
    }

    public ApplicationValidationException(String errorCode, String parms, RuntimeException t) {
        super(errorCode, t);
        this.errorCode = errorCode;
        this.parms = parms;
    }


    public String getParms() {
        return parms;
    }


    public String getErrorCode() {
        return errorCode;
    }
}
