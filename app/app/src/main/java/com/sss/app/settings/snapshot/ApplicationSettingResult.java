package com.sss.app.settings.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;
import com.sss.app.core.entity.snapshot.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSettingResult extends AbstractErrorSnapshot {

    private List<String> keys = new ArrayList<>();

    public ApplicationSettingResult() {
    }

    public ApplicationSettingResult(List<String> keys) {
        super();
        this.keys = keys;
    }

    public ApplicationSettingResult(ErrorResponse error) {
        super(error);
    }

    public void addKey(String key) {
        keys.add(key);
    }

    public void addKeys(List<String> keys) {
        this.keys.addAll(keys);
    }

    @JsonIgnore
    public String getKey() {
        if (keys.size() > 0)
            return keys.get(0);
        else
            return null;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}