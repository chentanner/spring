package com.sss.app.core.entity.model;

import com.sss.app.core.exception.ApplicationValidationException;

public interface Validatable {
    void validate() throws ApplicationValidationException;
}
