package com.sss.app.core.entity.snapshot;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractIdSnapshot extends AbstractErrorSnapshot implements Serializable, IEntitySnapshot {
    private BusinessKey businessKey;
    protected Integer entityId;

    public AbstractIdSnapshot() {
    }

    public AbstractIdSnapshot(Integer id) {
        this.entityId = id;
    }

    public AbstractIdSnapshot(ErrorResponse error) {
        super(error);
    }

    public void shallowCopyFrom(AbstractIdSnapshot copy) {
        super.shallowCopyFrom(copy);
        this.entityId = copy.entityId;
        this.businessKey = copy.businessKey;
    }

    public BusinessKey getBusinessKey() {
        if (businessKey != null)
            return businessKey;
        else if (entityId != null)
            return new BusinessKey(entityId);
        else
            return null;
    }

    public void setBusinessKey(BusinessKey businessKey) {
        this.businessKey = businessKey;
    }

    @Override
    public Integer getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIdSnapshot that = (AbstractIdSnapshot) o;
        return Objects.equals(businessKey, that.businessKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessKey);
    }
}
