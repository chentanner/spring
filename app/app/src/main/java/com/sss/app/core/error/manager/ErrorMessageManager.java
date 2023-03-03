package com.sss.app.core.error.manager;

import com.sss.app.core.entity.snapshot.ErrorResponse;
import com.sss.app.core.error.snapshot.ErrorMessageProjection;

import java.util.concurrent.ConcurrentHashMap;

public interface ErrorMessageManager {

    public static final String BEAN_NAME = "applicationErrorMessageManager";

    public static final String NO_ERROR_MSG_DEFINED = " no message defined";

    public void initialize();

    public ErrorResponse getErrorMessage(ErrorResponse error);

    public ErrorResponse getErrorMessage(ErrorResponse error, String exceptionMessage);

    public ErrorResponse getErrorMessage(ErrorResponse error, Exception exception);

    public void forget();

    public ConcurrentHashMap<String, ErrorMessageProjection> getErrorCodes(String errorCodePrefix);

}
