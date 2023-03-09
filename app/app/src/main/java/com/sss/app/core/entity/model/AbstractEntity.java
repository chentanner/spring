package com.sss.app.core.entity.model;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.repository.BaseRepository;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.exception.ApplicationValidationException;

import javax.persistence.Transient;

public abstract class AbstractEntity {

    @Transient
    public abstract String getEntityName();

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

    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
