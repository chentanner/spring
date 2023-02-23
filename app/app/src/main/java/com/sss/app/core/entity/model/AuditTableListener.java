package com.sss.app.core.entity.model;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditTableListener {

    /**
     * Called just before the history object is persisted.
     *
     * @param history - a domain object that represents History.
     */
    @PrePersist
    public void prePersist(AuditAbstractEntity history) {
        if (history.isRecentHistory())
            return;

        TemporalAbstractEntity parent = history.getParent();

        history.setIsExpired(parent.getIsExpired());

        history.copyFrom(parent);
        history.setEntityVersion(parent.getVersion());
    }

    /**
     * Called each time the object is flushed. This method may be called multiple times
     * before the transaction commits.
     *
     * @param history
     */
    @PreUpdate
    public void preUpdate(AuditAbstractEntity history) {
        if (history.isRecentHistory())
            return;
        TemporalAbstractEntity parent = history.getParent();

        if (parent == null)
            history.setIsExpired(true);
        else {
            history.setIsExpired(parent.getIsExpired());
            history.copyFrom(parent);
            history.setEntityVersion(parent.getVersion());
        }
    }


}
