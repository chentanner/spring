package com.sss.app.settings.model;

import com.sss.app.settings.enums.ApplicationSettingKey;

public class ApplicationSettingManagerBean extends BaseApplicationSettingManager {
    public ApplicationSettingManagerBean() {
        addKeyValuePair(ApplicationSettingKey.IS_TEST.getName(), "FALSE");
    }
}
