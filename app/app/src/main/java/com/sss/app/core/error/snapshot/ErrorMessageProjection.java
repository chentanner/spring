package com.sss.app.core.error.snapshot;

public class ErrorMessageProjection {

    private final String errorCode;

    private final String languageCode;

    private final String countryCode;

    private final String errorMessage;

    public ErrorMessageProjection(
            String errorCode,
            String languageCode,
            String countryCode,
            String errorMessage) {

        super();
        this.errorCode = errorCode;
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
