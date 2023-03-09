package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.parsing.Bracket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class WhereClause implements Serializable {
    private static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = 1L;
    public static final String LISTPARM_START = "(";
    public static final String LISTPARM_END = ")";
    public static final String LIST_SEPARATOR = ",";

    private int lastParamCounter = 1;
    private int expressionsCounter = 0;
    private Map<String, Object> params = new HashMap<>();
    private List<ExpressionElement> expressionElements = new ArrayList<>();
    private String queryEntityName;

    private final JoinClause joinClause;

    public WhereClause(JoinClause joinClause) {
        this.joinClause = joinClause;
    }

    public void addBracket(BracketElement bracket) {
        expressionElements.add(bracket);
    }

    public void addBracket(Bracket bracket) {
        expressionElements.add(new BracketElement(bracket));
    }

    public void validateBalancedBrackets() {
        int openCount = 0;
        int closeCount = 0;

        for (ExpressionElement e : expressionElements) {
            if (e instanceof BracketElement) {
                BracketElement bracketElement = (BracketElement) e;
                if (bracketElement.getBracket() == Bracket.OPEN)
                    openCount++;
                else
                    closeCount++;
            }
        }
        if (openCount != closeCount)
            throw new ApplicationRuntimeException(TransactionErrorCode.UNBALANCED_BRACKETS.getCode());
    }

    public void addConnector(ExpressionConnector connector) {
        expressionElements.add(new ConnectionElement(connector));
    }

    public void addConnector(ConnectionElement connector) {
        expressionElements.add(connector);
    }

    public void addExpression(WhereExpression whereExpression) {
        if (checkForExpired(whereExpression))
            return;
        if (whereExpression.getPropertyValue() != null)
            addParamFromExpression(whereExpression);

        processExpression(whereExpression);
    }

    private boolean checkForExpired(WhereExpression whereExpression) {
        if (whereExpression.getQueryProperty().getLabel().equals("expired")) {
            for (WhereExpression whereExpression2 : getWhereExpressions()) {
                if (whereExpression2.getQueryProperty().getLabel().equals("expired"))
                    return true;
            }
        }
        return false;
    }

    private void addParamFromExpression(WhereExpression whereExpression) {
        String alias = "p" + lastParamCounter++;
        whereExpression.setPropertyAlias(alias);
        setParameter(whereExpression);
    }

    public void setParameter(WhereExpression whereExpression) {
        params.put(whereExpression.getPropertyAlias(), whereExpression.getPropertyValue());
    }

    public void replaceParam(int position, Object replacementParam) {
        if (position > params.size())
            throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                  "position is outside param size.");

        String key = "p" + position;
        replaceTheParamAt(key, replacementParam);
    }

    public void replaceParams(List<?> replacementParams) {
        if (replacementParams.size() > params.size())
            throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                  "replacement params list greater than parameter list size");

        for (int i = 0; i < replacementParams.size(); i++) {
            int keyNo = (i + 1);
            String key = "p" + keyNo;
            Object replacementParam = replacementParams.get(i);
            replaceTheParamAt(key, replacementParam);
        }
    }

    protected void replaceTheParamAt(String key, Object replacementParam) {
        if (replacementParam == null)
            return;

        Object existingValue = params.get(key);
        QueryProperty queryProperty = fetchQueryPropertyFromAlias(key);

        if (existingValue == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                  "Cannot replace a null param value.");

        if (existingValue instanceof List<?>) {
            List<?> existingValueAsList = (List<?>) existingValue;
            if (existingValueAsList.size() == 0)
                throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                      "Cannot replace a null param value.");

            Object existingListItem = existingValueAsList.get(0);

            if (replacementParam instanceof List<?>) {
                List<?> replacementParamAsList = (List<?>) replacementParam;
                ArrayList<Object> newList = new ArrayList<>();
                for (Object listParam : replacementParamAsList) {
                    newList.add(convertParamToType(listParam, queryProperty));
                }
                params.put(key, newList);
            } else {
                List<Object> inList = extractParamList(replacementParam, existingListItem, queryProperty);
                params.put(key, inList);
            }
        } else {
            params.put(key, convertParamToType(replacementParam, queryProperty));
        }

        Object newValue = params.get(key);
        for (WhereExpression we : getWhereExpressions()) {
            if (we.getPropertyAlias().equals(key))
                we.setPropertyValue(newValue);
        }
    }

    public Map<String, QueryProperty> fetchQueryPropertiesAliasMap() {
        HashMap<String, QueryProperty> props = new HashMap<>();
        for (WhereExpression e : getWhereExpressions()) {
            props.put(e.getPropertyAlias(), e.getQueryProperty());
        }
        return props;
    }

    private QueryProperty fetchQueryPropertyFromAlias(String alias) {
        Map<String, QueryProperty> queryPropertyMap = fetchQueryPropertiesAliasMap();
        return queryPropertyMap.get(alias);
    }

    @JsonIgnore
    public List<WhereExpression> getWhereExpressions() {
        ArrayList<WhereExpression> whereExpressions = new ArrayList<>();
        for (ExpressionElement e : expressionElements) {
            if (e instanceof WhereExpression)
                whereExpressions.add((WhereExpression) e);
        }
        return whereExpressions;
    }

    public boolean hasWhereExpressions() {
        return getWhereExpressions().size() > 0;
    }

    public List<Object> convertParams(List<?> replacementParams) {
        List<Object> convertedParams = new ArrayList<>();
        Map<String, QueryProperty> queryPropertyMap = fetchQueryPropertiesAliasMap();

        if (replacementParams.size() > params.size())
            throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                  "replacement params list greater than parameter list size");

        for (int i = 0; i < replacementParams.size(); i++) {
            int keyNo = (i + 1);
            String key = "p" + keyNo;
            QueryProperty queryProperty = queryPropertyMap.get(key);
            Object replacementParam = replacementParams.get(i);
            Object currentParam = getQueryParameters().get(key);
            if (currentParam instanceof List<?>) {

                ArrayList<Object> newList = new ArrayList<>();
                if (!(replacementParam instanceof List<?>))
                    throw new ApplicationRuntimeException(TransactionErrorCode.QUERY_PROPERTY_DOES_NOT_EXIST,
                                                          "replacement param does not match existing param");
                List<?> convertList = (List<?>) replacementParam;
                for (Object p : convertList) {
                    newList.add(convertParamToType(p, queryProperty));
                }
                convertedParams.add(newList);
            } else {
                convertedParams.add(convertParamToType(replacementParam, queryProperty));
            }
        }
        return convertedParams;
    }


    public Object convertParamToType(Object replacementObject, QueryProperty queryProperty) {
        if (!(replacementObject instanceof String))
            return replacementObject;

        String replacementParam = (String) replacementObject;
        return queryProperty.convertValue(replacementParam);
    }

    private List<Object> extractParamList(Object replacementParam, Object existingListItem, QueryProperty queryProperty) {
        List<Object> list = new ArrayList<>();
        if (!(replacementParam instanceof String)) {
            list.add(replacementParam);
            return list;
        }
        String paramList = (String) replacementParam;

        if (paramList.equals(LISTPARM_START + LISTPARM_END))
            return list;

        if (!(paramList.startsWith(LISTPARM_START) && paramList.endsWith(LISTPARM_END))) {
            list.add(replacementParam);
            return list;
        }

        int positionOfEnd = paramList.lastIndexOf(LISTPARM_END);
        String subStr = paramList.substring(1, positionOfEnd);
        String[] items = subStr.split(LIST_SEPARATOR);
        for (String item : items)
            list.add(convertParamToType(item, queryProperty));
        return list;
    }


    /**
     * Add surrounding brackets to existing where expressions.
     */
    public void addBrackets() {
        if (!hasWhereExpressions())
            return;

        // add one at the beginning
        for (int i = 0; i < expressionElements.size(); i++) {
            ExpressionElement e = expressionElements.get(i);
            if (e instanceof WhereExpression) {

                expressionElements.add(i, new BracketElement(Bracket.OPEN));
                break;
            }
        }

        // add one after the end
        for (int i = expressionElements.size(); i > 0; i--) {
            ExpressionElement e = expressionElements.get(i - 1);
            if (e instanceof WhereExpression) {

                expressionElements.add(i, new BracketElement(Bracket.CLOSE));
                break;
            }
        }

    }


    public void addOrExpression(WhereExpression whereExpression) {
        if (checkForExpired(whereExpression))
            return;
        if (!getWhereExpressions().isEmpty())
            addConnector(ExpressionConnector.OR);
        if (whereExpression.getPropertyValue() != null)
            addParamFromExpression(whereExpression);

        processExpression(whereExpression);
    }

    public void addAndExpression(WhereExpression whereExpression) {
        if (checkForExpired(whereExpression))
            return;
        if (!getWhereExpressions().isEmpty())
            addConnector(ExpressionConnector.AND);
        if (whereExpression.getPropertyValue() != null)
            addParamFromExpression(whereExpression);

        processExpression(whereExpression);
    }

    private void processExpression(WhereExpression whereExpression) {
        expressionElements.add(whereExpression);
        whereExpression.setExpressionIndex(expressionsCounter++);
        QueryProperty queryProperty = whereExpression.getQueryProperty();

        if (!queryProperty.isPropertyInherited(getQueryEntityName())) {
            if (!queryEntityName.equals(queryProperty.getQueryEntityName())) {
                if (whereExpression.getJoinAlias() == null) {
                    logger.error("Error: QueryNames do not match " + queryEntityName + " != " + queryProperty.getQueryEntityName());
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_WHERE.getCode());
                }

                if (!joinClause.hasAlias(whereExpression.getJoinAlias())) {
                    logger.error("Error: QueryNames do not match " + queryEntityName + " != " + queryProperty.getQueryEntityName());
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_WHERE.getCode());
                }
            }
        }
    }

    public List<ExpressionElement> getExpressions() {
        return expressionElements;
    }

    public void setExpressions(List<ExpressionElement> expressionsIn) {
        for (ExpressionElement element : expressionsIn) {
            if (element instanceof WhereExpression) {
                WhereExpression whereExpression = (WhereExpression) element;
                addExpression(whereExpression);
            } else {
                expressionElements.add(element);
            }
        }
    }

    public String toString() {
        if (expressionElements.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder("WHERE ");
        for (ExpressionElement element : expressionElements) {
            sb.append(element.toString());
            sb.append(" ");
        }

        return sb.toString();
    }

    public String toFormattedString() {
        if (expressionElements.isEmpty())
            return "";

        StringBuilder sb = new StringBuilder();
        for (ExpressionElement element : expressionElements) {
            sb.append(element.toFormattedString());
            sb.append(" ");
        }

        return " WHERE " + sb;
    }


    public String toExternalString() {
        if (expressionElements.isEmpty())
            return "";

        validateBalancedBrackets();

        StringBuilder sb = new StringBuilder();
        for (ExpressionElement element : expressionElements) {
            sb.append(element.toExternalString());
            sb.append(" ");
        }

        return " WHERE " + sb;
    }

    @JsonIgnore
    public boolean hasQueryParameters() {
        return params.size() > 0;
    }

    @JsonIgnore
    public Map<String, Object> getQueryParameters() {
        HashMap<String, Object> result = new HashMap<>(params);
        for (WhereExpression whereExpression : getWhereExpressions()) {
            String key = whereExpression.getPropertyAlias();
            QueryPropertyType type = whereExpression.getQueryProperty().getType();
            if (result.containsKey(key) && type != null) {

                // Handle the scenario where the where expression is case-insensitive.
                if (whereExpression.getIgnoreCaseFlag() &&
                        type == QueryPropertyType.STRING) {
                    result.put(key, type.convertValue(result.get(key).toString().toLowerCase()));
                } else {
                    result.put(key, type.convertValue(result.get(key)));
                }
            }
        }
        return Collections.unmodifiableMap(result);
    }

    @JsonIgnore
    public boolean hasSelectionCriteria() {
        return !this.expressionElements.isEmpty();
    }

    @JsonIgnore
    public boolean hasQueryProperty(QueryProperty queryProperty) {
        for (WhereExpression ex : getWhereExpressions()) {
            if (ex.getQueryProperty().getName().equals(queryProperty.getName()))
                return true;
        }
        return false;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
        this.lastParamCounter = params.size() + 1;
    }

    public String getQueryEntityName() {
        return queryEntityName;
    }

    public void setQueryEntityName(String queryEntityName) {
        this.queryEntityName = queryEntityName;
    }

    public void addAndExpressionsFrom(List<ExpressionElement> elements) {
        if (expressionElements.size() > 0) {
            ExpressionElement lastElement = expressionElements.get(expressionElements.size() - 1);
            if (lastElement instanceof WhereExpression)
                addConnector(ExpressionConnector.AND);

            addBracket(new BracketElement(Bracket.OPEN));
            for (ExpressionElement element : elements) {
                addElement(element);
            }
            addBracket(new BracketElement(Bracket.CLOSE));
        } else {
            for (ExpressionElement element : elements) {
                addElement(element);
            }
        }
    }

    public void addExpressionsFrom(List<ExpressionElement> elements) {
        for (ExpressionElement element : elements) {
            addElement(element);
        }
    }


    private void addElement(ExpressionElement element) {
        if (element instanceof WhereExpression)
            addExpression((WhereExpression) element);
        else if (element instanceof BracketElement)
            addBracket((BracketElement) element);
        else if (element instanceof ConnectionElement)
            addConnector((ConnectionElement) element);
        else
            throw new ApplicationRuntimeException(TransactionErrorCode.UNSUPPORTED_ELEMENT);
    }

    public void copyFrom(WhereClause whereClauseFrom) {
        this.params = new HashMap<>();
        this.expressionElements = new ArrayList<>();
        this.queryEntityName = whereClauseFrom.queryEntityName;
        this.params.putAll(whereClauseFrom.params);
        this.expressionElements.addAll(whereClauseFrom.getExpressions());
        this.lastParamCounter = whereClauseFrom.lastParamCounter;
    }

}