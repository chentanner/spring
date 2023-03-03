package com.sss.app.core.error.manager;

import com.sss.app.core.entity.snapshot.ErrorResponse;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.error.model.ErrorMessageLang;
import com.sss.app.core.error.snapshot.ErrorMessageProjection;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service(value = ErrorMessageManager.BEAN_NAME)
public class ApplicationErrorMessageManager implements ErrorMessageManager {
    private final ConcurrentHashMap<String, ErrorMessageProjection> errorMap = new ConcurrentHashMap<>(2000);

    @Override
    public void initialize() {
        for (ErrorMessageProjection p : ErrorMessageLang.getErrorMessageProjections()) {
            errorMap.put(p.getErrorCode(), p);
        }
    }

    @Override
    public ErrorResponse getErrorMessage(ErrorResponse error) {
        if (error == null || error.getErrorCode() == null) {
            return new ErrorResponse(TransactionErrorCode.ERROR.getCode(),
                                     "Error code was null.  An error message could not be returned.");
        }

        String errorCode = error.getErrorCode();
        ErrorMessageProjection projection = errorMap.get(errorCode);
        if (projection == null) {
            error.setErrorMessage(errorCode + NO_ERROR_MSG_DEFINED);
        } else {
            error.setErrorMessage(projection.getErrorMessage());
        }
        return error;
    }

    @Override
    public ErrorResponse getErrorMessage(ErrorResponse error, String exceptionMessage) {
        error = getErrorMessage(error);

        if (exceptionMessage != null) {
            error.setErrorMessage(error.getErrorMessage() + " - " + exceptionMessage);
        }
        return error;
    }

    @Override
    public ErrorResponse getErrorMessage(ErrorResponse error, Exception exception) {
        return getErrorMessage(error, exception.getMessage());
    }

    @Override
    public void forget() {
        errorMap.clear();
    }

    @Override
    public ConcurrentHashMap<String, ErrorMessageProjection> getErrorCodes(String errorCodePrefix) {

        ConcurrentHashMap<String, ErrorMessageProjection> readOnlyErrorMap = new ConcurrentHashMap<>(2000);
        for (String errorCode : errorMap.keySet()) {
            if (errorCode.startsWith(errorCodePrefix)) {
                readOnlyErrorMap.put(errorCode, errorMap.get(errorCode));
            }
        }

        return readOnlyErrorMap;
    }
}
