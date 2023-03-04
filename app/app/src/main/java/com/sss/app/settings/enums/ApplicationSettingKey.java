package com.sss.app.settings.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ApplicationSettingKey {
    IS_TEST("IS_TEST");

    private final String name;

    private static final Map<String, ApplicationSettingKey> lookupByName
            = new HashMap<String, ApplicationSettingKey>();

    static {
        for (ApplicationSettingKey c : EnumSet.allOf(ApplicationSettingKey.class))
            lookupByName.put(c.name, c);
    }

    private ApplicationSettingKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ApplicationSettingKey lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
