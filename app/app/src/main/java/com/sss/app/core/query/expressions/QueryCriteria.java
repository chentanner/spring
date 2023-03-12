package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class QueryCriteria<T> {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_ALIAS = "entity";
    private static final String SIMPLELISTQUERY = "SELECT new com.sss.app.core.snapshot.SnapshotListItem(";
    private static final String HISTORY_DATE_RANGE_QRY = "SELECT new com.sss.app.core.entity.snapshot.HistoryDateRange(" +
            "                        entity.historyDateTimeStamp.validFromDateTime," +
            "                        entity.historyDateTimeStamp.validToDateTime," +
            "                        entity.historyDateTimeStamp.lastUserid," +
            "                        entity.auditComments) ";

    protected String queryEntityName;
    protected Class<T> queryEntityClass;
    private boolean isAudit = false;
    protected JoinClause joinClause = new JoinClause();
    protected WhereClause whereClause = new WhereClause(joinClause);
    protected OrderClause orderClause = new OrderClause();

    //    private QueryCriteria<T> secondaryQueryCriteria;
    private int maxResults = -1;

    private List<QueryProperty> selectProperties = new ArrayList<>();

    public QueryCriteria() {
    }


    public QueryCriteria(Class<T> clazz) {
        this.queryEntityName = clazz.getTypeName();
        this.queryEntityClass = clazz;
        whereClause.setQueryEntityName(this.queryEntityName);
    }

    @JsonIgnore
    public boolean hasSelectionCriteria() {
        return whereClause.hasSelectionCriteria() || orderClause.hasOrderByCriteria();
    }

    public String getQueryEntityName() {
        return queryEntityName;
    }

    public Class<T> getQueryEntityClass() {
        return queryEntityClass;
    }

    @JsonIgnore
    public boolean hasSelectionProperties() {
        return selectProperties.size() != 0;
    }

    public void addSelectionProperty(QueryProperty queryProperty) {
        selectProperties.add(queryProperty);
    }

    @JsonIgnore
    public boolean isSetMaxResults() {
        return maxResults > 0;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int setMaxResults) {
        this.maxResults = setMaxResults;
    }

//    public boolean hasSecondaryQueryCriteria() {
//        return secondaryQueryCriteria != null;
//    }
//
//    public QueryCriteria getSecondaryQueryCriteria() {
//        return secondaryQueryCriteria;
//    }
//
//
//    public void setSecondaryQueryCriteria(QueryCriteria secondaryQueryCriteria) {
//        this.secondaryQueryCriteria = secondaryQueryCriteria;
//    }


    public JoinClause getJoinClause() {
        return joinClause;
    }

    public void setJoinClause(JoinClause joinClause) {
        this.joinClause = joinClause;
    }

    public void copyFrom(QueryCriteria<T> queryCriteriaFrom) {
        this.queryEntityName = queryCriteriaFrom.queryEntityName;
        whereClause.setQueryEntityName(this.queryEntityName);
        this.whereClause.copyFrom(queryCriteriaFrom.getWhereClause());
        this.orderClause.copyFrom(queryCriteriaFrom.getOrderClause());
        this.joinClause.copyFrom(queryCriteriaFrom.joinClause);
        this.isAudit = queryCriteriaFrom.isAudit;
        selectProperties = new ArrayList<>();
        selectProperties.addAll(queryCriteriaFrom.getSelectProperties());
//        if (queryCriteriaFrom.hasSecondaryQueryCriteria()) {
//            secondaryQueryCriteria = new QueryCriteria();
//            secondaryQueryCriteria.copyFrom(queryCriteriaFrom.getSecondaryQueryCriteria());
//        }
    }

    public void replaceParams(List<?> replacementParams) {
        whereClause.replaceParams(replacementParams);
    }

    public void replaceParam(int position, Object replacementParam) {
        whereClause.replaceParam(position, replacementParam);
    }
//
//    public QueryCriteria(String queryEntityName, String secondaryQueryEntityName) {
//        this(queryEntityName);
//        secondaryQueryCriteria = new QueryCriteria(secondaryQueryEntityName);
//    }
//
//    public QueryCriteria(String queryEntityName, OrderExpression orderExpression) {
//        this.queryEntityName = queryEntityName;
//        whereClause.setQueryEntityName(this.queryEntityName);
//        this.addOrderExpression(orderExpression);
//    }
//
//    public QueryCriteria(String queryEntityName, OrderExpression[] orderExpressions) {
//        this.queryEntityName = queryEntityName;
//        whereClause.setQueryEntityName(this.queryEntityName);
//        if (orderExpressions != null) {
//            for (OrderExpression orderExpression : orderExpressions) {
//                this.addOrderExpression(orderExpression);
//            }
//        }
//    }
//
//    public QueryCriteria(
//            String queryEntityName,
//            WhereExpression[] addExpressions,
//            WhereExpression[] addAndExpressions,
//            WhereExpression[] addOrExpressions,
//            OrderExpression[] orderExpressions) {
//        this.queryEntityName = queryEntityName;
//        whereClause.setQueryEntityName(this.queryEntityName);
//        if (addExpressions != null) {
//            for (WhereExpression addExpression : addExpressions) {
//                this.getWhereClause().addExpression(addExpression);
//            }
//        }
//        if (addAndExpressions != null) {
//            for (WhereExpression addAndExpression : addAndExpressions) {
//                this.getWhereClause().addAndExpression(addAndExpression);
//            }
//        }
//        if (addOrExpressions != null) {
//            for (WhereExpression addOrExpression : addOrExpressions) {
//                this.getWhereClause().addOrExpression(addOrExpression);
//            }
//        }
//        if (orderExpressions != null) {
//            for (OrderExpression orderExpression : orderExpressions) {
//                this.addOrderExpression(orderExpression);
//            }
//        }
//    }

//    public void setQueryEntityName(String queryEntityName) {
//        this.queryEntityName = queryEntityName;
//        whereClause.setQueryEntityName(this.queryEntityName);
//    }

    public WhereClause getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(WhereClause whereClause) {
        this.whereClause = whereClause;
        whereClause.setQueryEntityName(this.queryEntityName);
    }

    @JsonIgnore
    public void addSecurityAuthorizationClause(QueryProperty queryProperty, List<Integer> secureIds) {
        WhereExpression whereExpression = new WhereExpression(queryProperty, ExpressionOperator.IN, secureIds);

        if (whereClause.hasSelectionCriteria())
            whereClause.addAndExpression(whereExpression);
        else
            whereClause.addExpression(whereExpression);
    }

    public OrderClause getOrderClause() {
        return orderClause;
    }

    public void setOrderClause(OrderClause orderClause) {
        this.orderClause = orderClause;
    }

    public void clearOrderClause() {
        orderClause.clear();
    }

    public void addWhereExpression(WhereExpression whereExpression) {
        whereClause.addExpression(whereExpression);
    }

    public void addOrderExpression(OrderExpression orderExpression) {
        orderClause.addOrderExpression(orderExpression);
    }

    public String toString() {
        return "FROM " + queryEntityName + " " + whereClause.toString() + " " + orderClause.toString();
    }

    public String toFormattedString() {
        return generateExternalFromStatement() + whereClause.toFormattedString() + " " + orderClause.toFormattedString();
    }

    public String toExternalString() {
        return generateExternalFromStatement() + whereClause.toExternalString() + " " + orderClause.toFormattedString();
    }

    protected String generateSelectStatement() {
        StringBuilder sb = new StringBuilder("SELECT ");
        if (hasSelectionProperties()) {
            sb.append(selectProperties.get(0).toString());
            for (int i = 1; i < selectProperties.size(); i++) {
                sb.append(", ");
                sb.append(selectProperties.get(i).toString());
            }
        } else {
            sb.append(" * ");
        }
        return sb.toString();
    }

    public String generateEntityQuery() {
        whereClause.validateBalancedBrackets();

        return "SELECT " + ENTITY_ALIAS + " " +
                generateFromStatement() +
                whereClause.toString() + " " + orderClause.toString();
    }

    protected String generateFromStatement() {
        return "FROM " +
                getDomainName() +
                " " +
                ENTITY_ALIAS +
                " " +
                joinClause.generateJoinStatement(getQueryEntityName());
    }

    protected String generateExternalFromStatement() {
        return "FROM " +
                queryEntityName +
                " " +
                joinClause.generateExternalJoinStatement(getQueryEntityName());
    }

    /**
     * Generates a query to return a count of entities.
     *
     * @return a list of entities
     */
    public String generateQueryForCount() {
        whereClause.validateBalancedBrackets();

        return "SELECT count(" + ENTITY_ALIAS + ")" + " " +
                generateFromStatement() +
                whereClause.toString();
    }

    /**
     * Generates a query to return a list of entities.
     *
     * @return a list of entities
     */
    public String generateQueryForIds() {
        whereClause.validateBalancedBrackets();

        return "SELECT " + ENTITY_ALIAS + ".id" + " " +
                generateFromStatement() +
                whereClause.toString() + " " + orderClause.toString();
    }

    /**
     * Generates a query to return a list of a single-valued domain property (a single column)
     *
     * @return a list of single valued properties
     */
    public String generateQueryForProperty(String propertyName) {
        whereClause.validateBalancedBrackets();

        return "SELECT " + ENTITY_ALIAS + "." + propertyName + " " +
                generateFromStatement() +
                whereClause.toString() + " " + orderClause.toString();
    }

    /**
     * Generates a query to return a list of a single-valued domain property (a single column)
     *
     * @return a list of single valued properties
     */
    public String generateQueryForProperties(List<String> propertyNames) {
        whereClause.validateBalancedBrackets();

        StringBuilder sb = new StringBuilder("SELECT ");

        if (propertyNames == null || propertyNames.size() == 0)
            return " ";

        sb.append(ENTITY_ALIAS);
        sb.append(".");
        sb.append(propertyNames.get(0));
        sb.append(" ");

        for (int i = 1; i < propertyNames.size(); i++) {
            sb.append(",");
            sb.append(ENTITY_ALIAS);
            sb.append(".");
            sb.append(propertyNames.get(i));
            sb.append(" ");
        }

        sb.append(generateFromStatement());
        sb.append(whereClause.toString());
        sb.append(" ");
        sb.append(orderClause.toString());

        return sb.toString();
    }

    /**
     * Generates a query to return a list of HistoryDateRanges from an audit entity
     *
     * @return a list of entities
     */
    public String generateAuditHistoryDateRangeQuery() {
        whereClause.validateBalancedBrackets();

        return HISTORY_DATE_RANGE_QRY + generateFromStatement() +
                whereClause.toString() + " " + orderClause.toString();
    }

    /**
     * Generate a query string that will return SimpleListItems.
     *
     * @param labelName       - name of property to use as the label used in simple list item
     * @param descriptionName - name of property to use as the description in simple list item
     * @return a query string
     */
    public String generateSimpleListQuery(String labelName, String descriptionName) {
        whereClause.validateBalancedBrackets();

        return SIMPLELISTQUERY +
                ENTITY_ALIAS + ".id, " +
                ENTITY_ALIAS + "." + labelName + ", " +
                ENTITY_ALIAS + "." + descriptionName + ", " +
                ENTITY_ALIAS + ".isExpired) " +
                generateFromStatement() +
                whereClause.toString() + " " + orderClause.toString();
    }

//    @JsonIgnore
//    public void setAuditQueryEntity(String primaryEntity) {
//        this.queryEntityName = primaryEntity;
//        isAudit = true;
//    }

    public List<QueryProperty> getSelectProperties() {
        return selectProperties;
    }

    public void setSelectProperties(List<QueryProperty> selectProperties) {
        this.selectProperties = selectProperties;
    }

    @JsonIgnore
    protected String getDomainName() {
        if (!isAudit)
            return queryEntityName;
        else
            return queryEntityName + "Audit";
    }

    /**
     * Add Surrounding Brackets to an existing set of where expressions.
     */
    public void AddBrackets() {
        whereClause.addBrackets();
    }

}
