package com.sss.app.core.query.parsing;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.expressions.JoinClause;
import com.sss.app.core.query.expressions.JoinType;
import com.sss.app.core.query.expressions.QueryProperty;
import com.sss.app.core.query.propertymapper.QueryPropertyMapper;

import java.util.Map;

public class QueryAsTextBaseEvaluator extends BaseTextParser {
    protected static final String WHERE = "WHERE";
    protected static final String JOIN = "JOIN";
    protected static final String OUTER_JOIN = "OUTER JOIN";
    protected static final String FROM = "FROM";
    protected static final String ORDER_BY = "ORDER BY";

    protected String queryEntityName;

    public QueryAsTextBaseEvaluator() {
    }

    public QueryAsTextBaseEvaluator(String queryEntityName) {
        this.queryEntityName = queryEntityName;
    }

    protected QueryAsTextInitialEvaluation parseExpression(String expressionText) {

        String trimmedText = expressionText.trim();

        QueryAsTextInitialEvaluation evaluation = new QueryAsTextInitialEvaluation();

        evaluation.indexOfFrom = trimmedText.indexOf(FROM + " ");
        evaluation.hasFromVerb = evaluation.indexOfFrom >= 0;

        evaluation.indexOfOuterJoin = trimmedText.indexOf(OUTER_JOIN);
        evaluation.hasOuterJoinVerb = evaluation.indexOfOuterJoin >= 0;


        if (evaluation.hasOuterJoinVerb) {
            evaluation.indexOfJoin = trimmedText.indexOf(JOIN + " ", evaluation.indexOfOuterJoin + 10);
        } else {
            evaluation.indexOfJoin = trimmedText.indexOf(JOIN);
        }
        evaluation.hasJoinVerb = evaluation.indexOfJoin >= 0;

        evaluation.indexOfWhere = trimmedText.indexOf(WHERE);
        evaluation.hasWhereClause = evaluation.indexOfWhere >= 0;

        evaluation.indexOfOrderBy = trimmedText.indexOf(ORDER_BY);
        evaluation.hasOrderByClause = evaluation.indexOfOrderBy >= 0;

        if (evaluation.hasOuterJoinVerb)
            evaluation.endOfFrom = evaluation.indexOfOuterJoin;
        else if (evaluation.hasJoinVerb)
            evaluation.endOfFrom = evaluation.indexOfJoin;
        else if (evaluation.hasWhereClause)
            evaluation.endOfFrom = evaluation.indexOfWhere;
        else if (evaluation.hasOrderByClause)
            evaluation.endOfFrom = evaluation.indexOfOrderBy;
        else
            evaluation.endOfFrom = trimmedText.length();

        if (evaluation.hasJoinVerb)
            evaluation.endOfOuterJoin = evaluation.indexOfJoin;
        else if (evaluation.hasWhereClause)
            evaluation.endOfOuterJoin = evaluation.indexOfWhere;
        else if (evaluation.hasOrderByClause)
            evaluation.endOfOuterJoin = evaluation.indexOfOrderBy;
        else
            evaluation.endOfOuterJoin = trimmedText.length();

        if (evaluation.hasWhereClause)
            evaluation.endOfJoin = evaluation.indexOfWhere;
        else if (evaluation.hasOrderByClause)
            evaluation.endOfJoin = evaluation.indexOfOrderBy;
        else
            evaluation.endOfJoin = trimmedText.length();

        if (evaluation.hasOrderByClause)
            evaluation.endOfWhere = evaluation.indexOfOrderBy;
        else
            evaluation.endOfWhere = trimmedText.length();

        return evaluation;
    }


    protected void buildJoinClause(
            JoinClause joinClause,
            JoinType joinType,
            String joinText) {
        String[] joinExpressions = joinText.split(",");
        for (String joinExpression : joinExpressions) {
            String trimmed = joinExpression.trim();
            String[] parts = trimmed.split(" ");
            String label = parts[0];
            String alias = parts[1];

            String propertyAlias = null;

            if (label.contains(".")) {
                String[] props = label.split("\\.");
                if (props.length < 2)
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PROPERTY);
                propertyAlias = props[0];
                label = props[1];
            }

            QueryProperty property;
            if (propertyAlias != null) {
                String joinQueryName = joinClause.getEntityNameFromAlias(propertyAlias);
                property = getQueryPropertyMap(joinQueryName).get(label);
            } else {
                property = getQueryPropertyMap().get(label);
            }

            if (property == null)
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PROPERTY);

            joinClause.addJoinDefinition(
                    joinType,
                    property,
                    alias);
        }

    }


    protected Map<String, QueryProperty> getQueryPropertyMap() {
        return getQueryPropertyMapper().getQueryMap(queryEntityName);
    }


    protected Map<String, QueryProperty> getQueryPropertyMap(String searchQueryName) {
        return getQueryPropertyMapper().getQueryMap(searchQueryName);
    }


    protected QueryPropertyMapper getQueryPropertyMapper() {
        return ApplicationContextFactory.getBean(QueryPropertyMapper.class);
    }


}
