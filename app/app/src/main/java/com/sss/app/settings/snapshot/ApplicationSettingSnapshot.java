package com.sss.app.settings.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApplicationSettingSnapshot implements Comparable<ApplicationSettingSnapshot> {
    private String key;
    private String value;
    private boolean isDelete = false;

    public ApplicationSettingSnapshot() {

    }

    public ApplicationSettingSnapshot(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonIgnore
    public boolean hasValue() {
        return (value != null);
    }


    @JsonIgnore
    public Integer getValueAsInteger() {
        if (!hasValue())
            return null;
        return Integer.parseInt(value);
    }

    @JsonIgnore
    public Long getValueAsLong() {
        if (!hasValue())
            return null;
        return Long.parseLong(value);
    }

    @JsonIgnore
    public boolean getValueAsBoolean() {
        if (!hasValue())
            return false;
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t");
    }

    @JsonIgnore
    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }


    @Override
    public int compareTo(ApplicationSettingSnapshot valueIn) {
        return this.key.compareTo(valueIn.key);
    }
}