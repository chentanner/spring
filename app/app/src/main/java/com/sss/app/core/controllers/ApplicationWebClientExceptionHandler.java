package com.sss.app.core.controllers;

import com.sss.app.core.exception.ApplicationWebClientException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(100)
@ControllerAdvice
public class ApplicationWebClientExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApplicationWebClientException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex,
            WebRequest request) {

        ApplicationWebClientException applicationWebClientException = (ApplicationWebClientException) ex;
        return handleExceptionInternal(
                ex,
                applicationWebClientException.getJsonBody(),
                new HttpHeaders(),
                applicationWebClientException.getHttpStatus(),
                request);
    }
}