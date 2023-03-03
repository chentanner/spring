package com.sss.app.core.controllers;

import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;
import com.sss.app.core.entity.snapshot.ErrorResponse;
import com.sss.app.core.error.manager.ErrorMessageManager;
import com.sss.app.core.snapshot.BaseSnapshotCollection;
import com.sss.app.core.snapshot.RestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;


public abstract class BaseRestController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    protected ErrorMessageManager errorMessageManager;

    private HttpHeaders getCustomHeaders() {
        return null;
    }

    private HttpHeaders getDefaultHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders customHeaders = getCustomHeaders();
        if (customHeaders != null) {
            return customHeaders;
        }
        return getDefaultHeader();
    }

    protected ResponseEntity<RestResult> assembleResultResponseEntity(RestResult result) {
        HttpHeaders headers = getHeaders();

        result.setError(getErrorMessage(result.getError()));

        if (result.wasSuccessful()) {
            return new ResponseEntity<>(result, headers, HttpStatus.OK);
        } else {
            if (result.isPermissionException()) {
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }
    }

    protected <T extends AbstractErrorSnapshot> ResponseEntity<T> assembleApplicationControlResultResponseEntity(T result) {
        HttpHeaders headers = getHeaders();

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (result.isPermissionException()) {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        if (!result.wasSuccessful()) {
            result.setError(getErrorMessage(result.getError()));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    protected <T> BaseSnapshotCollection<T> setSnapshotTransactionErrorCode(BaseSnapshotCollection<T> snapshot, ErrorResponse error) {
        snapshot.setError(getErrorMessage(error));

        return snapshot;
    }

    protected ErrorResponse getErrorMessage(ErrorResponse errorCode) {
        return errorMessageManager.getErrorMessage(errorCode);
    }

    protected ResponseEntity handleBindingErrors(
            BindingResult bindingResult,
            String message
    ) {
        logger.error(message);
        bindingResult.getAllErrors().forEach(
                error -> logger.error(error.toString())
        );

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
