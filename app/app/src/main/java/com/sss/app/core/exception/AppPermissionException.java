package com.sss.app.core.exception;

import com.sss.app.core.enums.TransactionErrorCode;

public class AppPermissionException extends RuntimeException {


    private String errorCode;
    private String parms = "";

    public AppPermissionException(TransactionErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode.getCode();
    }

    public AppPermissionException(TransactionErrorCode errorCode, String parm) {
        super(errorCode.getCode() + " " + parm);
        this.errorCode = errorCode.getCode();
        this.parms = parm;
    }

    public AppPermissionException(TransactionErrorCode errorCode, String parm, RuntimeException t) {
        super(errorCode.getCode() + " " + parm, t);
        this.errorCode = errorCode.getCode();
        this.parms = parm;
    }


    public AppPermissionException(TransactionErrorCode errorCode, RuntimeException t) {
        super(errorCode.getCode(), t);
        this.errorCode = errorCode.getCode();
    }

    public AppPermissionException(ApplicationValidationException e) {
        this.errorCode = e.getErrorCode();
    }


    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return getErrorMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean hasParms() {
        return (parms != null && !parms.isEmpty());
    }


    public String getParms() {
        return parms;
    }

    public String getErrorMessage() {
        StringBuilder buffer = new StringBuilder(errorCode);

        if (hasParms()) {
            buffer.append(": ");
            buffer.append(parms);
        }

        return buffer.toString();
    }


}
