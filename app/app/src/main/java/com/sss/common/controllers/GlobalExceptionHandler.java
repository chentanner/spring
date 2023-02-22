package com.sss.common.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger();

    @ExceptionHandler(value = {Exception.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex,
            WebRequest request) {

        if (request != null)
            logger.error("Runtime exception occurred: " + request.getContextPath(), ex);
        else
            logger.error("Runtime exception occurred.", ex);

        String bodyOfResponse = "Application internal error";
        return handleExceptionInternal(
                ex,
                bodyOfResponse,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

}
