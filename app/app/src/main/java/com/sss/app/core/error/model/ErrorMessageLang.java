package com.sss.app.core.error.model;

import com.sss.app.core.entity.model.AbstractEntity;
import com.sss.app.core.error.snapshot.ErrorMessageProjection;
import com.sss.app.core.query.QueryParameters;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ERROR_MSG_CODE_LANG")
@NamedQueries({
        @NamedQuery(
                name = ErrorMessageLang.FIND_ALL,
                query = "    SELECT new com.sss.app.core.error.snapshot.ErrorMessageProjection("
                        + "message.errorCode, " +
                        "message.languageCode, " +
                        "message.countryCode, " +
                        "message.errorMessage) " +
                        "     FROM  ErrorMessageLang message " +
                        "    WHERE  message.languageCode = :languageCode " +
                        "      AND  message.countryCode = :countryCode " +
                        " ORDER BY  message.errorCode"),
})
public class ErrorMessageLang extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final String FIND_ALL = "find all errormessages";

    private String errorCode;

    private String languageCode;

    private String countryCode;

    private String errorMessage;

    @Id
    @Column(name = "ERROR_MSG_CD")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Id
    @Column(name = "LOCALE_LANGUAGE_CD")
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Column(name = "LOCALE_COUNTRY_CD")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "ERROR_MSG_CODE_DESC")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static List<ErrorMessageProjection> getErrorMessageProjections() {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.add("languageCode", "en");
        queryParameters.add("countryCode", "CA");
        return getBaseDAO().executeQuery(
                ErrorMessageProjection.class,
                FIND_ALL,
                queryParameters);
    }

    @Override
    @Transient
    public String getEntityName() {
        return "ErrorMessageLang";
    }
}


