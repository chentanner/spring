package com.sss.app.core.entity.snapshot;

import com.sss.app.core.entity.enums.EntityState;

public interface VersionedEntitySnapshot {
    public Long getVersion();

    public boolean isVersioned();

    public void setVersion(long version);

    public EntityState getEntityState();

    public Boolean isExpired();
}
