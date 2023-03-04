package com.sss.app.settings.model;

import java.util.Map;

public interface ApplicationSettingManager {
    public boolean hasValue(String key);

    public String getValue(String key);

    public Map<String, String> getKeyValueMap();
}
