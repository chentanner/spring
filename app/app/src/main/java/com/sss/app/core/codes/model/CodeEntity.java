package com.sss.app.core.codes.model;

public interface CodeEntity {
    public static String DEFAULT_COUNTRY_CODE = "CA";
    public static String DEFAULT_LANGUAGE_CODE = "EN";

    public String getCode();

    public String getLabel();

    public Integer getDisplayOrderNo();
}
