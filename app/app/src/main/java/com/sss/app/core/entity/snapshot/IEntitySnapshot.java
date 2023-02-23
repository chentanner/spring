package com.sss.app.core.entity.snapshot;

public interface IEntitySnapshot {

    public Integer getEntityId();

    public void setEntityId(Integer id);

    public boolean isVersioned();

    public long getVersion();

    public void setVersion(long version);
}
