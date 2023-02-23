package com.sss.app.core.entity.repository;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.query.QueryParameters;

import java.util.List;

public class EntityRepository<T> implements IEntityRepository {
    public List<Integer> fetchIds() {
        return List.of();
    }

    private Class<T> clazz;

    public EntityRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T load(int id) {
        return getBaseDAO().load(clazz, id);
    }

    public List<T> fetchAll(String queryName) {
        return getBaseDAO().executeQuery(clazz, queryName, new QueryParameters());
    }

    public T executeSingleResultQuery(String queryName) {
        return getBaseDAO().executeSingleResultQuery(clazz, queryName);
    }

    public List<T> executeQuery(String queryName) {
        return getBaseDAO().executeQuery(clazz, queryName);
    }

    protected static BaseRepository getBaseDAO() {

        return ApplicationContextFactory.getBean(BaseRepository.class);
    }
}
