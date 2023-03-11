package com.sss.app.core.query.propertymapper;

import com.sss.app.core.query.expressions.QueryProperty;

import java.util.List;
import java.util.Map;

public interface QueryPropertyMapper {

    public Map<String, QueryProperty> getQueryMap(String queryEntityName);

    public List<QueryProperty> getQueryProperties(String queryEntityName);

    public BaseQueryProperties getBase(String queryEntityName);

    public List<String> getQueryEntityNames();

    public List<String> getFilteredSortedJoinProperties(
            String queryEntityName,
            String filter);

    public List<String> getFilteredSortedQueryProperties(
            String queryEntityName,
            String filter,
            long start,
            int limit);
}