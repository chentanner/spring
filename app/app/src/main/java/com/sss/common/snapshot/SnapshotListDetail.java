package com.sss.common.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SnapshotListDetail implements Serializable {

    private String name;
    private String description;
    private Boolean isExpired = false;

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public Boolean getIsExpired() {
        return isExpired;
    }

    @JsonIgnore
    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

}
