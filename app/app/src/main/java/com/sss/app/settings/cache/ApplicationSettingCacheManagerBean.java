package com.sss.app.settings.cache;

import com.sss.app.settings.model.ApplicationSetting;
import com.sss.app.settings.model.ApplicationSettingManager;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ApplicationSettingCacheManagerBean implements ApplicationSettingCacheManager {
    private static final Logger logger = LogManager.getLogger();

    private final ConcurrentHashMap<String, String> cachedApplicationSettings = new ConcurrentHashMap<>(100, .75f);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationSettingManager applicationSettingsManager;

    @Override
    public List<ApplicationSettingSnapshot> fetchApplicationSettings() {
        HashMap<String, String> newApplicationSettings = new HashMap<>(100, .75f);
        newApplicationSettings.putAll(cachedApplicationSettings);

        return newApplicationSettings
                .entrySet()
                .stream()
                .map(c -> new ApplicationSettingSnapshot(c.getKey(), c.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void initialize() {
        cachedApplicationSettings.putAll(applicationSettingsManager.getKeyValueMap());
        ApplicationSetting.loadAll()
                .forEach(c -> cachedApplicationSettings.put(
                        c.getKey(),
                        c.getValue()));
    }

    @Override
    public void reload() {
        HashMap<String, String> newApplicationSettings = new HashMap<>(100, .75f);
        cachedApplicationSettings.putAll(applicationSettingsManager.getKeyValueMap());

        ApplicationSetting.loadAll()
                .forEach(c -> newApplicationSettings.put(
                        c.getKey(),
                        c.getValue()));

        cachedApplicationSettings.putAll(newApplicationSettings);
    }

    @Override
    public String getValue(String key) {

        return cachedApplicationSettings.get(key);
    }

    @Override
    public void setValue(String key, String value) {
        cachedApplicationSettings.put(key, value);
    }

    @Override
    public void setValue(String key, long i) {
        String value = Long.toString(i);
        cachedApplicationSettings.put(key, value);
    }

    @Override
    public void setValue(String key, BigDecimal bg) {
        String value = bg.toPlainString();
        cachedApplicationSettings.put(key, value);
    }

    @Override
    public Integer getValueAsInt(String key) {
        String value = cachedApplicationSettings.get(key);
        if (value == null)
            return null;
        return Integer.decode(value);
    }

    @Override
    public void setValue(String key, int i) {
        String value = Integer.toString(i);
        cachedApplicationSettings.put(key, value);
    }

    @Override
    public Long getValueAsLong(String key) {
        String value = cachedApplicationSettings.get(key);
        if (value == null)
            return null;

        return Long.decode(value);
    }

    @Override
    public void saveApplicationSettings(Map<String, String> acEntries) {
        cachedApplicationSettings.putAll(acEntries);
    }

    @Override
    public void saveApplicationSettings(List<ApplicationSettingSnapshot> controls) {
        HashMap<String, String> newApplicationSettings = new HashMap<>(100, .75f);
        controls.forEach(c -> newApplicationSettings.put(c.getKey(), c.getValue()));
        cachedApplicationSettings.putAll(newApplicationSettings);
    }

    @Override
    public Boolean getValueAsBoolean(String key) {
        String value = cachedApplicationSettings.get(key);
        if (value == null)
            return false;
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t");
    }

    @Override
    public void setValue(String key, boolean bool) {
        cachedApplicationSettings.put(key, Boolean.valueOf(bool).toString());
    }

    @Override
    public BigDecimal getValueAsBigDecimal(String key) {
        String value = cachedApplicationSettings.get(key);
        if (value == null)
            return null;
        return new BigDecimal(value);
    }

}
