package com.sss.app.core.query.propertymapper;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.expressions.QueryProperty;
import com.sss.app.core.query.expressions.QueryPropertyType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseQueryProperties {
    private static final Logger logger = LogManager.getLogger();

    public static final QueryProperty expiredQueryProperty = new QueryProperty("AbstractEntity",
                                                                               "expired",
                                                                               "isExpired",
                                                                               QueryPropertyType.BOOLEAN);

    private String queryEntityName;

    private final List<QueryProperty> queryProperties = new ArrayList<>();

    private final Map<String, QueryProperty> queryMap = new HashMap<>();

    public BaseQueryProperties(String queryEntityName) {
        this.queryEntityName = queryEntityName;
        add(expiredQueryProperty);
        queryMap.put(expiredQueryProperty.getLabel(), expiredQueryProperty);
    }

    public BaseQueryProperties(String queryEntityName, boolean hasExpiryProperty) {
        this.queryEntityName = queryEntityName;
        if (hasExpiryProperty) {
            add(expiredQueryProperty);
            queryMap.put(expiredQueryProperty.getLabel(), expiredQueryProperty);
        }
    }

    protected void add(QueryProperty queryProperty) {

        if (queryProperty.getLabel().equals("expired") && queryMap.containsKey("expired")) {
            logger.debug("expired already there");
            return;
        }

        queryProperties.add(queryProperty);
        queryMap.put(queryProperty.getLabel(), queryProperty);

        if (queryProperty.getQueryEntityName() == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_PROPERTY_ENTITY);
    }

    public List<QueryProperty> getQueryProperties() {
        return queryProperties;
    }

    public Map<String, QueryProperty> getQueryMap() {
        return queryMap;
    }

    public QueryProperty get(String label) {
        return queryMap.get(label);
    }

    public String fetchQueryEntityName() {
        return queryEntityName;
    }

    public void setQueryEntityName(String queryEntityName) {
        this.queryEntityName = queryEntityName;
    }
}
