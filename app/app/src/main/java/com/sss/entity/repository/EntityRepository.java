package com.sss.entity.repository;

import com.sss.entity.managers.ApplicationContextFactory;

import java.util.List;

public class EntityRepository implements IEntityRepository {
    public List<Integer> fetchIds() {
        return List.of();
    }


    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
