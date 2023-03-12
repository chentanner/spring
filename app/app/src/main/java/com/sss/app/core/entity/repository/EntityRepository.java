package com.sss.app.core.entity.repository;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.query.expressions.QueryCriteria;

import java.util.List;

public abstract class EntityRepository<T> implements IEntityRepository<T> {
    public List<Integer> fetchIds() {
        return List.of();
    }

    private final Class<T> clazz;

    public EntityRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T load(int id) {
        return getBaseDAO().load(clazz, id);
    }

    public List<T> fetchAll() {
        return getBaseDAO().executeEntityQueryCriteria(new QueryCriteria<>(clazz));
    }

    public T findSingleResult(QueryCriteria<T> queryCriteria) {
        return getBaseDAO().executeSingleResultEntityQueryCriteria(queryCriteria);
    }

    public List<T> find(QueryCriteria<T> queryCriteria) {
        return getBaseDAO().executeEntityQueryCriteria(queryCriteria);
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
