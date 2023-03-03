package com.sss.app.core.entity.snapshot;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.core.entity.model.VersionedEntity;

import java.io.Serializable;

public abstract class AbstractEntitySnapshot extends AbstractIdSnapshot implements Serializable, VersionedEntity {
    private final long DEFAULT_VERSION = -1L;
    protected Long version = DEFAULT_VERSION;

    protected EntityState entityState = EntityState.NEW;

    protected boolean isExpired;

    public AbstractEntitySnapshot() {
    }

    public AbstractEntitySnapshot(Integer id, Boolean isExpired) {
        super(id);
        this.isExpired = isExpired;
    }

    public AbstractEntitySnapshot(ErrorResponse error) {
        super(error);
    }

    public void shallowCopyFrom(AbstractEntitySnapshot copy) {
        super.shallowCopyFrom(copy);
        this.version = copy.version;
        this.entityState = copy.entityState;
        this.isExpired = copy.isExpired;
    }

    @Override
    public boolean isVersioned() {
        return version >= 0;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }

    public EntityState getEntityState() {
        return entityState;
    }

    public void setEntityState(EntityState entityState) {
        this.entityState = entityState;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
