package com.sss.app.core.entity.snapshot;

public class EntitySnapshot implements IEntitySnapshot {
    private Integer entityId;
    private long version;

    @Override
    public Integer getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean isVersioned() {
        return version >= 0;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }
}
