package com.sss.app.settings.serviceimpl;

import com.sss.app.core.entity.snapshot.TransactionResult;
import com.sss.app.settings.model.ApplicationSetting;
import com.sss.app.settings.service.ApplicationSettingService;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ApplicationSettingServiceBean implements ApplicationSettingService {

    @Override
    public ApplicationSettingSnapshot fetchApplicationSetting(String key) {
        ApplicationSetting setting = ApplicationSetting.fetch(key);
        ApplicationSettingSnapshot value = new ApplicationSettingSnapshot();

        if (setting != null) {
            value.setKey(setting.getKey());
            value.setValue(setting.getValue());
            return value;
        } else {
            return null;
        }
    }

    @Override
    public TransactionResult saveApplicationSetting(ApplicationSettingSnapshot value) {
        ApplicationSetting.saveUpdate(value);
        return new TransactionResult();
    }

    @Override
    public List<ApplicationSettingSnapshot> fetchApplicationSettings() {
        List<ApplicationSetting> settings = ApplicationSetting.loadAll();
        List<ApplicationSettingSnapshot> values = new ArrayList<>();

        for (ApplicationSetting setting : settings) {
            ApplicationSettingSnapshot value = new ApplicationSettingSnapshot();
            value.setKey(setting.getKey());
            value.setValue(setting.getValue());
            values.add(value);
        }
        return values;
    }

    @Override
    public TransactionResult saveApplicationSettings(List<ApplicationSettingSnapshot> applicationSettingss) {

        TransactionResult result = new TransactionResult();
        for (ApplicationSettingSnapshot ApplicationSettingSnapshot : applicationSettingss) {

            ApplicationSetting.saveUpdate(ApplicationSettingSnapshot);
        }
        return result;
    }
}
