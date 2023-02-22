package com.sss.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sss.common.snapshot.ErrorResponse;
import com.sss.enums.TransactionErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ApplicationExceptionFactoryBean implements ApplicationExceptionFactory {

    @Autowired
    private ObjectMapper objectMapper;

    public ApplicationWebClientException newApplicationWebClientExceptionTimeout(String target) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(TransactionErrorCode.WEB_REST_CLIENT_FAILED.name());
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            return new ApplicationWebClientException(HttpStatus.GATEWAY_TIMEOUT, json);
        } catch (JsonProcessingException e) {
            return new ApplicationWebClientException(HttpStatus.BAD_REQUEST, "{}");
        }
    }

}
