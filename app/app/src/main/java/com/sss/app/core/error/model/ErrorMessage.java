package com.sss.app.core.error.model;

import com.sss.app.core.entity.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ERROR_MSG_CODE")
public class ErrorMessage extends AbstractEntity {

    private String errorCode;

    @Id
    @Column(name = "ERROR_MSG_CD")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
