package com.sss.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public interface ListItem extends Serializable {

    @JsonProperty
    public Integer getId();

    @JsonProperty
    public String getCode();

    @JsonProperty
    public String getDescription();

    @JsonProperty
    public Boolean getExpired();
}