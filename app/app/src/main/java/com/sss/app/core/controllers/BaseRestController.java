package com.sss.app.core.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;


public abstract class BaseRestController {
    private static final Logger logger = LogManager.getLogger();

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
}
