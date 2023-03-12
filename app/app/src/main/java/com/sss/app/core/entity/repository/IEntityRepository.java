package com.sss.app.core.entity.repository;

import com.sss.app.core.query.expressions.QueryCriteria;

import java.util.List;

public interface IEntityRepository<T> {
    public T load(int id);

    public List<Integer> fetchIds();

    public List<T> fetchAll();

    public List<T> executeQuery(String queryName);

    public T executeSingleResultQuery(String queryName);

    public List<T> find(QueryCriteria<T> queryCriteria);

    public T findSingleResult(QueryCriteria<T> queryCriteria);

}
