package com.sss.app.core.codes.model;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class CodeEntityPk implements Serializable {

    protected String code;
    protected String localeLanguageCode;
    protected String localeCountryCode;

    @Column(name = "CODE_CD")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "LOCALE_LANGUAGE_CD")
    public String getLocaleLanguageCode() {
        return localeLanguageCode;
    }

    public void setLocaleLanguageCode(String languageCode) {
        this.localeLanguageCode = languageCode;
    }

    @Column(name = "LOCALE_COUNTRY_CD")
    public String getLocaleCountryCode() {
        return localeCountryCode;
    }

    public void setLocaleCountryCode(String countryCode) {
        this.localeCountryCode = countryCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, localeLanguageCode, localeCountryCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeEntityPk that = (CodeEntityPk) o;
        return Objects.equals(code, that.code) && Objects.equals(localeLanguageCode,
                                                                 that.localeLanguageCode) && Objects.equals(
                localeCountryCode,
                that.localeCountryCode);
    }

    @Override
    public String toString() {
        return "CodeEntityPk{" +
                "code='" + code + '\'' +
                ", localeLanguageCode='" + localeLanguageCode + '\'' +
                ", localeCountryCode='" + localeCountryCode + '\'' +
                '}';
    }

}