package com.sss.app.core.query.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinClause {

    private final Map<String, String> aliasMap = new HashMap<String, String>();

    private List<JoinDefinition> joinDefinitions = new ArrayList<JoinDefinition>();


    public void copyFrom(JoinClause copy) {
        this.joinDefinitions.addAll(copy.joinDefinitions);
    }

    public boolean hasAlias(String alias) {
        return aliasMap.containsKey(alias);
    }

    public boolean hasJoinDefinitions() {
        return joinDefinitions.size() > 0;
    }

    public Map<String, String> getAliasMap() {
        HashMap<String, String> map = new HashMap<>();
        for (JoinDefinition d : joinDefinitions) {
            map.put(d.getAlias(), d.getQueryProperty().getJoinQueryEntityName());
        }
        return map;
    }

    public void addJoinDefinition(QueryProperty queryProperty, String alias) {

        aliasMap.put(alias, queryProperty.getJoinQueryEntityName());
        joinDefinitions.add(
                new JoinDefinition(
                        queryProperty,
                        alias));
    }

    public void addJoinDefinition(
            JoinType joinType,
            QueryProperty queryProperty,
            String alias) {

        aliasMap.put(alias, queryProperty.getJoinQueryEntityName());

        joinDefinitions.add(
                new JoinDefinition(
                        joinType,
                        queryProperty,
                        alias));
    }


    public List<JoinDefinition> getJoinDefinitions() {
        return joinDefinitions;
    }

    public void setJoinDefinitions(List<JoinDefinition> joinDefinitions) {
        this.joinDefinitions = joinDefinitions;
    }

    public String generateJoinStatement(String queryEntityName) {
        StringBuilder buffer = new StringBuilder();
        for (JoinDefinition joinDefinition : joinDefinitions) {
            if (joinDefinition.getQueryProperty().getQueryEntityName().equals(queryEntityName)) {
                buffer.append(joinDefinition);
            } else {
                String joinAlias = getAliasFromEntityName(joinDefinition.getQueryProperty().getQueryEntityName());
                buffer.append(joinDefinition.toStringWithAlias(joinAlias));
            }
            buffer.append(" ");
        }
        return buffer.toString();
    }


    public String generateExternalJoinStatement(String queryEntityName) {
        ArrayList<JoinDefinition> outerJoins = new ArrayList<>();
        ArrayList<JoinDefinition> joins = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();

        for (JoinDefinition joinDefinition : joinDefinitions) {
            if (joinDefinition.getJoinType() == JoinType.JOIN)
                joins.add(joinDefinition);
            else
                outerJoins.add(joinDefinition);
        }

        if (outerJoins.size() > 0) {
            buffer.append("OUTER JOIN ");
            generateExternalJoinStatements(
                    buffer,
                    outerJoins,
                    queryEntityName);
        }

        if (joins.size() > 0) {
            buffer.append(" JOIN ");
            generateExternalJoinStatements(
                    buffer,
                    joins,
                    queryEntityName);
        }

        return buffer.toString();
    }

    private void generateExternalJoinStatements(
            StringBuffer buffer,
            List<JoinDefinition> filteredDefinitions,
            String queryEntityName) {

        if (filteredDefinitions.get(0).getQueryProperty().getQueryEntityName().equals(queryEntityName)) {
            buffer.append(filteredDefinitions.get(0).toExternalString());
        } else {
            String joinAlias = getAliasFromEntityName(filteredDefinitions.get(0).getQueryProperty().getQueryEntityName());
            buffer.append(filteredDefinitions.get(0).toExternalStringWithAlias(joinAlias));
        }
        for (int i = 1; i < filteredDefinitions.size(); i++) {
            buffer.append(",");
            JoinDefinition joinDefinition = joinDefinitions.get(i);
            if (joinDefinition.getQueryProperty().getQueryEntityName().equals(queryEntityName)) {
                buffer.append(joinDefinition.toExternalString());
            } else {
                String joinAlias = getAliasFromEntityName(joinDefinition.getQueryProperty().getQueryEntityName());
                buffer.append(joinDefinition.toExternalStringWithAlias(joinAlias));
            }
        }
    }


    public String getAliasFromEntityName(String entityName) {
        for (String key : aliasMap.keySet()) {
            String name = getAliasMap().get(key);
            if (name.equals(entityName))
                return key;
        }
        return null;
    }

    public String getEntityNameFromAlias(String alias) {
        return aliasMap.get(alias);
    }

}