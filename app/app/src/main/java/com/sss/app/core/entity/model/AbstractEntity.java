package com.sss.app.core.entity.model;

import com.sss.app.core.entity.repository.BaseRepository;
import com.sss.app.core.entity.managers.ApplicationContextFactory;

import javax.validation.ValidationException;

public abstract class AbstractEntity implements IEntity {
    public abstract Integer getId();

    @Override
    public Boolean isExpired() {
        return false;
    }

    public AbstractEntity save() {
        try {
            validate();
        } catch (ValidationException ignored) {

        }
        getBaseDAO().save(this);
        return this;
    }

    public AbstractEntity update() {
        try {
            validate();
        } catch (ValidationException ignored) {

        }
        return this;
    }


    public AbstractEntity validate() throws ValidationException {
        return this;
    }

    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
