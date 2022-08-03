package com.sss.entity.repository;

import com.sss.entity.model.AbstractEntity;
import com.sss.query.QueryParameter;
import com.sss.query.QueryParameters;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class BaseRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    public <T> T load(Class<T> claz, Integer id) {
        return entityManager.find(claz, id);
    }

    public <T> List<T> executeQuery(String queryName) {
        Query query = entityManager.createNamedQuery(queryName);
        return (List<T>) query.getResultList();
    }

    public <T> List<T> executeQuery(String queryName, Class<T> claz) {
        return executeQuery(queryName, new QueryParameters(), claz);
    }

    public <T> List<T> executeQuery(String queryName,
                                    QueryParameters parameters,
                                    Class<T> claz) {
        TypedQuery<T> query = entityManager.createNamedQuery(queryName, claz);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList();
    }

    public void save(List<AbstractEntity> entities) {
        entities.forEach(AbstractEntity::save);
    }

    public void save(AbstractEntity entity) {
        entityManager.persist(entity);
    }

    public void delete(AbstractEntity entity) {
        entityManager.remove(entity);
    }

    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    public void flush() {
        entityManager.flush();
    }

    public void refresh(AbstractEntity entity) {
        entityManager.refresh(entity);
    }
}
