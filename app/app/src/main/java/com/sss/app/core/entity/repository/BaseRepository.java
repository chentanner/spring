package com.sss.app.core.entity.repository;

import com.sss.app.core.entity.model.AbstractEntity;
import com.sss.app.core.entity.model.AbstractIdEntity;
import com.sss.app.core.entity.model.AuditAbstractEntity;
import com.sss.app.core.entity.model.ExpiryPolicy;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.QueryParameter;
import com.sss.app.core.query.QueryParameters;
import com.sss.app.core.query.expressions.QueryCriteria;
import com.sss.app.core.snapshot.SimpleListItem;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class BaseRepository {
    public static final int MAX_SIZE_FOR_IN = 1000;
    private ExpiryPolicy defaultExpiryPolicy;
    private Map<String, ExpiryPolicy> expiryPolicies = new HashMap<>();

    @PersistenceContext
    protected EntityManager entityManager;

    public <T> T load(Class<T> claz, Integer id) {
        return entityManager.find(claz, id);
    }

    public <T> T load(Class<T> claz, String id) {
        return entityManager.find(claz, id);
    }

    public <T> T loadNonEntity(Class<T> claz, Object id) {
        return entityManager.find(claz, id);
    }

    public <T> T executeSingleResultQuery(Class<T> claz, String queryName) {
        return executeSingleResultQuery(claz, queryName, new QueryParameters());
    }

    public <T> T executeSingleResultQuery(Class<T> claz,
                                          String queryName,
                                          QueryParameters parameters) {
        TypedQuery<T> query = entityManager.createNamedQuery(queryName, claz);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        List<T> results = query.getResultList();
        if (results.size() == 1)
            return results.get(0);
        else if (results.size() == 0)
            return null;
        else
            throw new ApplicationRuntimeException(TransactionErrorCode.MORE_THAN_ONE_OBJECT_RETURNED_QUERY.getCode());
    }

    public <T> List<T> executeQuery(Class<T> claz, String queryName) {
        return executeQuery(claz, queryName, new QueryParameters());
    }

    public <T> List<T> executeQuery(Class<T> claz,
                                    String queryName,
                                    QueryParameters parameters) {
        TypedQuery<T> query = entityManager.createNamedQuery(queryName, claz);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList();
    }

    //TODO:
//        query.setMaxResults(1);
//        query.setMaxResults(maxResults);
//


    public List<SimpleListItem> executeSimpleListQuery(String queryName,
                                                       QueryParameters parameters) {
        TypedQuery<SimpleListItem> query = entityManager.createNamedQuery(queryName, SimpleListItem.class);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList();
    }

//    public List<SimpleListItem> executeSimpleListQueryFromQueryCriteria(
//            QueryCriteria queryCriteria,
//            String labelName,
//            String descriptionName) {
//
//        Query query = entityManager.createQuery(queryCriteria.generateSimpleListQuery(labelName, descriptionName));
//
//        if (queryCriteria.getWhereClause().hasQueryParameters()) {
//            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
//                query.setParameter(entry.getKey(), entry.getValue());
//            }
//        }
//        return query.getResultList();
//    }

    public Long executeNamedCountQuery(String queryName,
                                       QueryParameters parameters) {
        TypedQuery<Long> query = entityManager.createNamedQuery(queryName, Long.class);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList().get(0);
    }

    public BigDecimal executeNamedSumQuery(String queryName,
                                           QueryParameters parameters) {
        TypedQuery<BigDecimal> query = entityManager.createNamedQuery(queryName, BigDecimal.class);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList().get(0);
    }

    public <T> List<T> executeEntityQueryCriteria(QueryCriteria<T> queryCriteria) {
        TypedQuery<T> query = entityManager.createQuery(queryCriteria.generateEntityQuery(),
                                                        queryCriteria.getQueryEntityClass());
        if (queryCriteria.getMaxResults() > 0)
            query.setMaxResults(queryCriteria.getMaxResults());

        if (queryCriteria.getWhereClause().hasQueryParameters()) {
            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    public <T> T executeSingleResultEntityQueryCriteria(QueryCriteria<T> queryCriteria) {
        TypedQuery<T> query = entityManager.createQuery(queryCriteria.generateEntityQuery(),
                                                        queryCriteria.getQueryEntityClass());
        if (queryCriteria.getMaxResults() > 0)
            query.setMaxResults(queryCriteria.getMaxResults());

        if (queryCriteria.getWhereClause().hasQueryParameters()) {
            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        List<T> results = query.getResultList();
        if (results.size() == 1)
            return results.get(0);
        else if (results.size() == 0)
            return null;
        else
            throw new ApplicationRuntimeException(TransactionErrorCode.MORE_THAN_ONE_OBJECT_RETURNED_QUERY.getCode());
    }

    public <T> Integer executeCountQueryCriteria(QueryCriteria<T> queryCriteria) {
        Query query = entityManager.createQuery(queryCriteria.generateQueryForCount());

        if (queryCriteria.getWhereClause().hasQueryParameters()) {
            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return ((Long) query.getSingleResult()).intValue();
    }

    public List<Integer> executeIdQueryCriteria(QueryCriteria<?> queryCriteria) {
        TypedQuery<Integer> query = entityManager.createQuery(queryCriteria.generateQueryForIds(), Integer.class);

        if (queryCriteria.getWhereClause().hasQueryParameters()) {
            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        if (queryCriteria.isSetMaxResults())
            query.setMaxResults(queryCriteria.getMaxResults());

        return query.getResultList();
    }

//    public List executePropertiesQueryCriteria(QueryCriteria queryCriteria, List<String> propertyNames) {
//        Query query = entityManager.createQuery(queryCriteria.generateQueryForProperties(propertyNames));
//
//        if (queryCriteria.getWhereClause().hasQueryParameters()) {
//            for (Map.Entry<String, ?> entry : queryCriteria.getWhereClause().getQueryParameters().entrySet()) {
//                query.setParameter(entry.getKey(), entry.getValue());
//            }
//        }
//        return query.getResultList();
//    }

    public List executeNativeQuery(final String nativeQueryString,
                                   QueryParameters parameters) {
        Query query = entityManager.createNativeQuery(nativeQueryString);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.getResultList();
    }

    public int executeUpdate(String queryText,
                             QueryParameters parameters) {
        Query query = entityManager.createQuery(queryText);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.executeUpdate();
    }

    public int executeNativeUpdate(String queryText,
                                   QueryParameters parameters) {
        Query query = entityManager.createNativeQuery(queryText);
        for (QueryParameter parameter : parameters) {
            query.setParameter(parameter.getName(), parameter.getValue());
        }
        return query.executeUpdate();
    }

    public <T> void executeDelete(Class<T> claz,
                                  String queryName,
                                  QueryParameters parameters) {
        try {
            entityManager.remove(executeSingleResultQuery(claz, queryName, parameters));
        } catch (DataAccessException e) {
            throw new ApplicationRuntimeException(TransactionErrorCode.DELETE_FAILED.getCode(), e);
        }
    }

    public ExpiryPolicy getDefaultExpiryPolicy() {
        return defaultExpiryPolicy;
    }

    public void setDefaultExpiryPolicy(ExpiryPolicy expiryPolicy) {
        this.defaultExpiryPolicy = expiryPolicy;
    }

    public Map<String, ExpiryPolicy> getExpiryPolicies() {
        return expiryPolicies;
    }

    public void setExpiryPolicies(Map<String, ExpiryPolicy> expiryPolicies) {
        this.expiryPolicies = expiryPolicies;
    }

    public ExpiryPolicy getExpiryPolicy(String entityClassName) {
        if (expiryPolicies == null)
            return defaultExpiryPolicy;

        ExpiryPolicy expirePolicy = expiryPolicies.get(entityClassName);
        if (expirePolicy != null)
            return expirePolicy;

        return defaultExpiryPolicy;
    }


    public void save(List<AbstractIdEntity> entities) {
        entities.forEach(AbstractIdEntity::save);
    }

    public void save(AbstractEntity entity) {
        entityManager.persist(entity);
    }

    public void saveWithHistory(AuditAbstractEntity history) {
        if (history != null)
            entityManager.persist(history);
    }

    public void delete(AbstractEntity entity) {
        entityManager.remove(entity);
    }

    public void flush() {
        entityManager.flush();
    }

    public void refresh(AbstractIdEntity entity) {
        entityManager.refresh(entity);
    }
}
