package com.sss.app.core.entity.snapshot;

import com.sss.app.core.entity.model.VersionedEntity;

public interface IEntitySnapshot extends VersionedEntity {

    public BusinessKey getBusinessKey();

    public void setBusinessKey(BusinessKey businessKey);

    public Integer getEntityId();

    public void setEntityId(Integer id);
}
