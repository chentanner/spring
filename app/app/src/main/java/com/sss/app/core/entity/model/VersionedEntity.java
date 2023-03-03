package com.sss.app.core.entity.model;

import com.sss.app.core.entity.enums.EntityState;

public interface VersionedEntity extends IEntity {
    public Long getVersion();

    public boolean isVersioned();

    public void setVersion(long version);

    public EntityState getEntityState();

    public Boolean getIsExpired();
}
