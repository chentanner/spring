package com.sss.app.settings.cache;

import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ApplicationSettingCacheManager {
    public List<ApplicationSettingSnapshot> fetchApplicationSettings();

    public void saveApplicationSettings(Map<String, String> acEntries);

    public void saveApplicationSettings(List<ApplicationSettingSnapshot> controls);

    public String getValue(String key);

    public void setValue(String key, String value);

    public Integer getValueAsInt(String key);

    public void setValue(String key, int i);

    public Long getValueAsLong(String key);

    public void setValue(String key, long i);

    public Boolean getValueAsBoolean(String key);

    public void setValue(String key, boolean bool);

    public BigDecimal getValueAsBigDecimal(String key);

    public void setValue(String key, BigDecimal bg);

    public void reload();

    public void initialize();
}
