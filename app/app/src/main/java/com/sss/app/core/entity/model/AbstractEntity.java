package com.sss.app.core.entity.model;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.repository.BaseRepository;

public abstract class AbstractEntity {
    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
