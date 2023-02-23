package com.sss.app.core.entity.model;

public interface VersionedAuditEntity {
    public Long getEntityVersion();

    public void setEntityVersion(Long entityVersion);

    public Long getVersion();
}
