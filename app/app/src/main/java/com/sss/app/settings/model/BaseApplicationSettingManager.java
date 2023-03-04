package com.sss.app.settings.model;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseApplicationSettingManager implements ApplicationSettingManager {
    private final HashMap<String, String> defaultValues = new HashMap<>();

    protected void addKeyValuePair(String key, String value) {
        defaultValues.put(key, value);
    }

    public boolean hasValue(String key) {
        return defaultValues.containsKey(key);
    }

    public String getValue(String key) {
        return defaultValues.get(key);
    }

    public Map<String, String> getKeyValueMap() {
        return defaultValues;
    }
}
