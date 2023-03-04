package com.sss.app.settings.adapterimpl;

import com.sss.app.core.entity.snapshot.TransactionResult;
import com.sss.app.core.snapshot.RestResult;
import com.sss.app.settings.adapter.ApplicationSettingRestAdapter;
import com.sss.app.settings.cache.ApplicationSettingCacheManager;
import com.sss.app.settings.service.ApplicationSettingService;
import com.sss.app.settings.snapshot.ApplicationSettingResult;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshotCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationSettingRestAdapterBean implements ApplicationSettingRestAdapter {

    @Autowired
    private ApplicationSettingService applicationSettingService;

    @Autowired
    private ApplicationSettingCacheManager applicationSettingCacheManager;

    @Override
    public RestResult reloadApplicationSettingCache() {
        applicationSettingCacheManager.reload();
        return new RestResult();
    }

    @Override
    public ApplicationSettingSnapshotCollection fetchApplicationSettings(
            long start,
            int limit) {

        List<ApplicationSettingSnapshot> values = applicationSettingService.fetchApplicationSettings();

        List<ApplicationSettingSnapshot> sorted = values.stream().sorted().collect(Collectors.toList());
        return new ApplicationSettingSnapshotCollection(
                sorted,
                0,
                100,
                values.size());
    }

    @Override
    public ApplicationSettingSnapshotCollection findApplicationSettings(
            String filter,
            long start,
            int limit) {

        List<ApplicationSettingSnapshot> values = applicationSettingService.fetchApplicationSettings();

        List<ApplicationSettingSnapshot> sorted = values.stream().sorted().filter(c -> c.getKey().startsWith(filter)).collect(
                Collectors.toList());
        return new ApplicationSettingSnapshotCollection(
                sorted,
                0,
                100,
                values.size());
    }

    @Override
    public ApplicationSettingSnapshotCollection fetchCachedApplicationSettings(
            long start,
            int limit) {

        List<ApplicationSettingSnapshot> values = applicationSettingCacheManager.fetchApplicationSettings();

        List<ApplicationSettingSnapshot> sorted = values.stream().sorted().collect(Collectors.toList());
        return new ApplicationSettingSnapshotCollection(
                sorted,
                0,
                100,
                values.size());
    }

    @Override
    public ApplicationSettingSnapshotCollection findCachedApplicationSettings(
            String filter,
            long start,
            int limit) {

        List<ApplicationSettingSnapshot> values = applicationSettingCacheManager.fetchApplicationSettings();

        List<ApplicationSettingSnapshot> sorted = values.stream().sorted().filter(c -> c.getKey().startsWith(filter)).collect(
                Collectors.toList());
        return new ApplicationSettingSnapshotCollection(
                sorted,
                0,
                100,
                values.size());
    }

    @Override
    public ApplicationSettingResult saveApplicationSetting(ApplicationSettingSnapshot setting) {
        applicationSettingService.saveApplicationSetting(setting);
        return new ApplicationSettingResult();
    }

    @Override
    public ApplicationSettingResult saveCachedApplicationSetting(ApplicationSettingSnapshot setting) {
        applicationSettingCacheManager.setValue(setting.getKey(), setting.getValue());
        return new ApplicationSettingResult();
    }

    @Override
    public ApplicationSettingResult saveCachedApplicationSettings(List<ApplicationSettingSnapshot> settings) {
        applicationSettingCacheManager.saveApplicationSettings(settings);
        return new ApplicationSettingResult();
    }

    @Override
    public ApplicationSettingResult saveApplicationSettings(List<ApplicationSettingSnapshot> settings) {
        TransactionResult result = applicationSettingService.saveApplicationSettings(settings);
        return new ApplicationSettingResult();
    }
}
