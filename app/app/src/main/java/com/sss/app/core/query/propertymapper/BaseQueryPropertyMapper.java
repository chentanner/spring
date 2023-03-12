package com.sss.app.core.query.propertymapper;

import com.sss.app.core.query.expressions.QueryProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseQueryPropertyMapper implements QueryPropertyMapper {

    private final Map<String, BaseQueryProperties> queryEntityToQueryMap = new HashMap<>();

    public BaseQueryPropertyMapper() {
    }

    @Override
    public List<String> getQueryEntityNames() {
        return queryEntityToQueryMap
                .keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public void addQueryPropertyMap(BaseQueryProperties baseQueryProperties) {
        queryEntityToQueryMap.put(baseQueryProperties.fetchQueryEntityName(), baseQueryProperties);
    }

    public Map<String, QueryProperty> getQueryMap(String queryEntityName) {
        BaseQueryProperties base = queryEntityToQueryMap.get(queryEntityName);
        if (base != null)
            return base.getQueryMap();
        else
            return null;
    }

    @Override
    public BaseQueryProperties getBase(String queryEntityName) {
        return queryEntityToQueryMap.get(queryEntityName);
    }

    @Override
    public List<QueryProperty> getQueryProperties(String queryEntityName) {
        return queryEntityToQueryMap.get(queryEntityName).getQueryProperties();
    }

    @Override
    public List<String> getFilteredSortedJoinProperties(
            String queryEntityName,
            String filter) {

        if (!filter.isBlank()) {
            return getQueryProperties(queryEntityName)
                    .stream()
                    .sorted()
                    .filter(QueryProperty::isJoinProperty)
                    .filter(c -> c.contains(filter))
                    .map(QueryProperty::getLabel)
                    .collect(Collectors.toList());
        } else {
            return getQueryProperties(queryEntityName)
                    .stream()
                    .sorted()
                    .filter(QueryProperty::isJoinProperty)
                    .map(QueryProperty::getLabel)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<String> getFilteredSortedQueryProperties(
            String queryEntityName,
            String filter,
            long start,
            int limit) {

        List<String> sortedFiltered;

        if (!filter.isBlank()) {
            sortedFiltered = getQueryProperties(queryEntityName)
                    .stream()
                    .sorted()
                    .filter(c -> !c.isJoinProperty())
                    .filter(c -> c.contains(filter))
                    .map(QueryProperty::getLabel)
                    .collect(Collectors.toList());
        } else {
            sortedFiltered = getQueryProperties(queryEntityName)
                    .stream()
                    .sorted()
                    .filter(c -> !c.isJoinProperty())
                    .map(QueryProperty::getLabel)
                    .collect(Collectors.toList());
        }

        int toIndex = (int) (start + limit);

        int totalIds = sortedFiltered.size();

        if (toIndex > totalIds)
            toIndex = (int) totalIds;
        int fromIndex = (int) start;


        return sortedFiltered.subList(fromIndex, toIndex);
    }
}
