package com.sss.app.core.entity.model;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.repository.BaseRepository;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.exception.ApplicationValidationException;

public abstract class AbstractEntity implements IEntity {
    public abstract Integer getId();

    @Override
    public Boolean isExpired() {
        return false;
    }

    public void save() {
        try {
            validate();
        } catch (ApplicationValidationException ve) {
            throw new ApplicationRuntimeException(ve);
        }
        getBaseDAO().save(this);
    }

    public void update() {
        try {
            validate();
        } catch (ApplicationValidationException ve) {
            throw new ApplicationRuntimeException(ve);
        }
    }


    public void validate() throws ApplicationValidationException {
    }

    protected static AuditManager getAuditManager() {
        return ApplicationContextFactory.getBean(AuditManager.class);
    }

    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
