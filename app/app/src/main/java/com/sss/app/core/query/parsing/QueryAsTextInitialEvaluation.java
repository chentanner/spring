package com.sss.app.core.query.parsing;

public class QueryAsTextInitialEvaluation {

    boolean hasFromVerb = false;
    boolean hasWhereClause = false;
    boolean hasOrderByClause = false;
    boolean hasJoinVerb = false;
    boolean hasOuterJoinVerb = false;

    int indexOfFrom = -1;
    int endOfFrom = -1;
    int indexOfOuterJoin = -1;
    int endOfOuterJoin = -1;
    int indexOfJoin = -1;
    int endOfJoin = -1;
    int indexOfWhere = -1;
    int endOfWhere = -1;
    int indexOfOrderBy = -1;
    int endOfOrderBy = -1;


    public boolean hasNoClauses() {
        return !(hasJoinVerb || hasOuterJoinVerb || hasWhereClause || hasOrderByClause);
    }

}
