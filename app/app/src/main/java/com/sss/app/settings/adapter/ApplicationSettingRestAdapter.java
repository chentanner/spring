package com.sss.app.settings.adapter;

import com.sss.app.core.snapshot.RestResult;
import com.sss.app.settings.snapshot.ApplicationSettingResult;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshot;
import com.sss.app.settings.snapshot.ApplicationSettingSnapshotCollection;

import java.util.List;

public interface ApplicationSettingRestAdapter {

    public ApplicationSettingSnapshotCollection fetchApplicationSettings(
            long start,
            int limit);

    public ApplicationSettingSnapshotCollection findApplicationSettings(
            String filter,
            long start,
            int limit);

    public ApplicationSettingSnapshotCollection fetchCachedApplicationSettings(
            long start,
            int limit);

    public ApplicationSettingSnapshotCollection findCachedApplicationSettings(
            String filter,
            long start,
            int limit);

    public RestResult reloadApplicationSettingCache();

    public ApplicationSettingResult saveApplicationSetting(ApplicationSettingSnapshot setting);

    public ApplicationSettingResult saveApplicationSettings(List<ApplicationSettingSnapshot> settings);

    public ApplicationSettingResult saveCachedApplicationSetting(ApplicationSettingSnapshot setting);

    public ApplicationSettingResult saveCachedApplicationSettings(List<ApplicationSettingSnapshot> settings);

}
