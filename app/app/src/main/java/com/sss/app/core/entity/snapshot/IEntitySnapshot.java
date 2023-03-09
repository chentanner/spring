package com.sss.app.core.entity.snapshot;

public interface IEntitySnapshot extends VersionedEntitySnapshot {

    public BusinessKey getBusinessKey();

    public void setBusinessKey(BusinessKey businessKey);

    public Integer getEntityId();

    public void setEntityId(Integer id);
}
