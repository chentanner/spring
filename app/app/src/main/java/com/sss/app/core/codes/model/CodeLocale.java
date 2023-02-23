package com.sss.app.core.codes.model;

import javax.persistence.*;

@Entity
@Table(name = "LOCALE_CODE")
public class CodeLocale implements CodeEntity {

    private String countryCode;
    private String languageCode;
    private String localeCode;
    private String LocaleCodeDescription;
    private Boolean isDefaultLanguageLocale;

    public CodeLocale() {
    }

    @Id
    @Column(name = "LOCALE_CD")
    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    @Column(name = "COUNTRY_CD")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String code) {
        this.countryCode = code;
    }

    @Column(name = "LANGUAGE_CD")
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Column(name = "LOCALE_CODE_DESC")
    public String getLocaleCodeDescription() {
        return LocaleCodeDescription;
    }

    public void setLocaleCodeDescription(String localeCodeDescription) {
        LocaleCodeDescription = localeCodeDescription;
    }

    @Column(name = "DEFAULT_LANGUAGE_LOCALE_FLG")
    @org.hibernate.annotations.Type(type = "yes_no")
    public Boolean getIsDefaultLanguageLocale() {
        return isDefaultLanguageLocale;
    }

    public void setIsDefaultLanguageLocale(Boolean isDefaultLanguageLocale) {
        this.isDefaultLanguageLocale = isDefaultLanguageLocale;
    }

    @Transient
    public String getCode() {
        return this.languageCode;
    }

    @Transient
    /**
     * Display order is not implemented on this class yet.
     */
    public Integer getDisplayOrderNo() {
        return null;
    }

    @Transient
    public String getDescription() {
        return this.LocaleCodeDescription;
    }

    @Transient
    public String getLabel() {
        return localeCode;
    }
}
