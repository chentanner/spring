package com.sss.exception;

import com.sss.enums.TransactionErrorCode;

public class ApplicationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;
    private String parms = "";

    public ApplicationRuntimeException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ApplicationRuntimeException(TransactionErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
    }

    public ApplicationRuntimeException(TransactionErrorCode errorCode, RuntimeException t) {
        super(errorCode.getCode(), t);
        this.errorCode = errorCode.getCode();
    }

    public ApplicationRuntimeException(TransactionErrorCode errorCode, String parms) {
        this.errorCode = errorCode.getCode();
        this.parms = parms;
    }


    public ApplicationRuntimeException(String errorCode, String parm) {
        super(errorCode + " " + parm);
        this.errorCode = errorCode;
        this.parms = parm;
    }

    public ApplicationRuntimeException(String errorCode, String parm, RuntimeException t) {
        super(errorCode + " " + parm, t);
        this.errorCode = errorCode;
        this.parms = parm;
    }

    public ApplicationRuntimeException(String errorCode, String parm, ApplicationValidationException t) {
        super(errorCode + " " + parm, t);
        this.errorCode = errorCode;
        this.parms = parm;
    }


    public ApplicationRuntimeException(String errorCode, RuntimeException t) {
        super(errorCode, t);
        this.errorCode = errorCode;
    }

    public ApplicationRuntimeException(ApplicationValidationException v) {
        this.errorCode = v.getErrorCode();
        this.parms = v.getParms();
    }


    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return getErrorMessage();
    }

    public boolean hasParms() {
        return (parms != null && (parms.equals("") == false));
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getParms() {
        return parms;
    }


    public String getErrorMessage() {
        return parms;
    }
}
