package com.sss.app.core.query.expressions;

public class JoinDefinition {

    private final QueryProperty queryProperty;
    private final String alias;

    private JoinType joinType = JoinType.JOIN;

    public JoinDefinition(
            QueryProperty queryProperty,
            String alias) {
        this.queryProperty = queryProperty;
        this.alias = alias;
    }


    public JoinDefinition(
            JoinType joinType,
            QueryProperty queryProperty,
            String alias) {

        this.joinType = joinType;
        this.queryProperty = queryProperty;
        this.alias = alias;
    }


    public QueryProperty getQueryProperty() {
        return queryProperty;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getAlias() {
        return alias;
    }


    public String toFormattedString() {
        return toExternalString();
    }

    public String toString() {
        return joinType.getType() + " " + "entity." + queryProperty.getName() + " AS " + alias;
    }

    public String toStringWithAlias(String joinAlias) {
        return joinType.getType() + " " + joinAlias + "." + queryProperty.getName() + " AS " + alias;
    }

    public String toExternalStringWithAlias(String joinAlias) {
        return joinAlias + "." + queryProperty.getLabel() + " " + alias;
    }

    public String toExternalString() {
        return queryProperty.getLabel() + " " + alias;
    }
}
