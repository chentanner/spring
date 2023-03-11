package com.sss.app.core.entity.model;

import com.sss.app.core.entity.snapshot.BusinessKey;

import javax.persistence.Transient;

public abstract class AbstractIdEntity extends AbstractEntity implements IdEntity {
    public abstract Integer getId();

    @Transient
    public BusinessKey getBusinessKey() {
        if (getId() == null) {
            return null;
        } else {
            return new BusinessKey(getId());
        }
    }
}
