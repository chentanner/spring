package com.sss.app.settings.service;

import com.sss.app.core.entity.snapshot.TransactionResult;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;

import java.util.List;

public interface ApplicationSettingService {
    public ApplicationSettingSnapshot fetchApplicationSetting(String key);

    public TransactionResult saveApplicationSetting(ApplicationSettingSnapshot value);

    public List<ApplicationSettingSnapshot> fetchApplicationSettings();

    public TransactionResult saveApplicationSettings(List<ApplicationSettingSnapshot> applicationSettings);

}
