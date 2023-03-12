package com.sss.app.core.query.parsing;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.expressions.*;
import com.sss.app.core.query.parsing.operator.AssignmentOperator;
import com.sss.app.core.query.parsing.operator.ComparisonOperator;
import com.sss.app.core.query.parsing.token.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class QueryAsTextParser<T> extends QueryAsTextBaseEvaluator<T> {
    private static final Logger logger = LogManager.getLogger();

    public QueryAsTextParser() {
    }

    public QueryAsTextParser(Class<T> queryEntityClass) {
        super(queryEntityClass);
    }

    public QueryCriteria<T> parse(String expressionText) {

        String trimmedText = expressionText.trim();
        if (trimmedText.equals(""))
            return new QueryCriteria<>(queryEntityClass);

        QueryAsTextInitialEvaluation evaluation = super.parseExpression(expressionText);

        QueryCriteria<T> queryCriteria;

        if (evaluation.hasFromVerb) {
            String fromText = trimmedText.substring(evaluation.indexOfFrom + 5, evaluation.endOfFrom);
            queryCriteria = buildFromClause(fromText);
        } else {
            queryCriteria = new QueryCriteria<>(queryEntityClass);
        }

        if (!evaluation.hasOrderByClause && !evaluation.hasWhereClause) {

            if (!expressionText.isEmpty()) {
                if (!evaluation.hasFromVerb)
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                          expressionText);
            }

            return new QueryCriteria<>(queryEntityClass);
        }

        if (evaluation.hasJoinVerb) {

            if ((evaluation.endOfJoin - evaluation.indexOfJoin) > 5) {
                String joinText = trimmedText.substring(evaluation.indexOfJoin + 5, evaluation.endOfJoin);

                buildJoinClause(
                        queryCriteria.getJoinClause(),
                        JoinType.JOIN,
                        joinText);
            }
        }
        if (evaluation.hasOuterJoinVerb) {

            if ((evaluation.endOfOuterJoin - evaluation.indexOfOuterJoin) > 11) {
                String joinText = trimmedText.substring(evaluation.indexOfOuterJoin + 11, evaluation.endOfOuterJoin);

                buildJoinClause(
                        queryCriteria.getJoinClause(),
                        JoinType.LEFT_OUTER_JOIN,
                        joinText);
            }
        }

        if (evaluation.hasWhereClause) {
            if ((evaluation.endOfWhere - evaluation.indexOfWhere) > 5) {
                String whereExpressionText = trimmedText.substring(evaluation.indexOfWhere + 6, evaluation.endOfWhere);
                buildWhereClause(queryCriteria, whereExpressionText.trim());
            }
        }

        if (evaluation.hasOrderByClause) {
            String orderByText = trimmedText.substring(evaluation.indexOfOrderBy + 9);
            buildOrderClause(queryCriteria, orderByText);
        }


        return queryCriteria;
    }

    private QueryCriteria<T> buildFromClause(String fromText) throws ApplicationRuntimeException {
        String queryEntityNameStr = fromText.trim();
        if (queryEntityName != null) {
            if (!queryEntityName.equals(queryEntityNameStr.trim()))
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_ENTITY_NAMES_MISMATCH,
                                                      "QueryEntityNames don't match: " + queryEntityName + " != " + queryEntityNameStr);
        } else {
            queryEntityName = queryEntityNameStr.trim();
        }

        return new QueryCriteria<>(queryEntityClass);
    }

    private void buildOrderClause(QueryCriteria<T> queryCriteria, String orderByText) throws ApplicationRuntimeException {

        String[] orderExpressions = orderByText.split(",");

        for (String s : orderExpressions) {
            String token = s.trim();
            String[] tokens = token.split(" ");

            QueryProperty queryProperty;

            String name = tokens[0];

            if (name.contains(".")) {
                String[] parts = name.split("\\.");
                String alias = parts[0];
                if (parts.length > 1)
                    name = parts[1];
                String searchQueryEntityName = queryCriteria.getJoinClause().getEntityNameFromAlias(alias);
                queryProperty = getQueryPropertyMap(searchQueryEntityName).get(name);
            } else {
                queryProperty = getQueryPropertyMap().get(name);
            }

            if (queryProperty == null)
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PROPERTY,
                                                      "Unknown order by property:" + tokens[0]);
            if (tokens.length > 1) {
                OrderOperator orderOp = OrderOperator.lookup(tokens[1]);

                if (orderOp == null)
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_OPERATOR,
                                                          "Unknown order operator" + tokens[1]);

                queryCriteria.getOrderClause().addOrderExpression(
                        new OrderExpression(
                                orderOp,
                                queryProperty));
            } else {
                queryCriteria.getOrderClause().addOrderExpression(
                        new OrderExpression(queryProperty));
            }
        }

    }

    private void buildWhereClause(
            QueryCriteria<T> queryCriteria,
            String expressionText) throws ApplicationRuntimeException {

        QueryAsTextReader reader;
        if (queryCriteria.getJoinClause().hasJoinDefinitions()) {
            reader = new QueryAsTextReader(
                    queryCriteria.getJoinClause().getAliasMap(),
                    queryEntityName,
                    expressionText);
        } else {
            reader = new QueryAsTextReader(
                    queryEntityName,
                    expressionText);
        }

        reader.parse();
        List<ParsingQueryNode> nodes = reader.getTokenList();
        List<ParsingQueryNode> whereNodes = processNodesForWhereClause(nodes);

        if (whereNodes.isEmpty())
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_WHERE,
                                                  expressionText);

        buildExpressions(
                queryCriteria,
                whereNodes);
    }

    public List<ParsingQueryNode> processNodesForWhereClause(List<ParsingQueryNode> nodesIn) throws ApplicationRuntimeException {
        List<ParsingQueryNode> nodes = convertInLists(nodesIn);
        logList("convertIn", nodes);

        nodes = convertPrimitiveOperators(nodes);
        logList("convertOperators", nodes);

        nodes = convertWhereExpressions(nodes);
        logList("convertExpressions", nodes);

        nodes = convertWhereExpressionsContainingWithinOperators(nodes);

        return nodes;
    }

    public void buildExpressions(
            QueryCriteria<T> queryCriteria,
            List<ParsingQueryNode> nodes) throws ApplicationRuntimeException {
        int openCount = 0;
        int closeCount = 0;

        List<ParsingQueryNode> badNodes = new ArrayList<>();

        for (ParsingQueryNode node : nodes) {

            if (node instanceof OpenBracketToken) {
                OpenBracketToken t = (OpenBracketToken) node;
                openCount++;
                queryCriteria.getWhereClause().addBracket(new BracketElement(t.getBracket()));
            } else if (node instanceof CloseBracketToken) {
                CloseBracketToken t = (CloseBracketToken) node;
                closeCount++;
                queryCriteria.getWhereClause().addBracket(new BracketElement(t.getBracket()));
            } else if (node instanceof ExpressionConnectorWrapper) {
                ExpressionConnectorWrapper w = (ExpressionConnectorWrapper) node;
                queryCriteria.getWhereClause().addConnector(new ConnectionElement(w.getConnector()));
            } else if (node instanceof WhereExpressionWrapper) {
                WhereExpressionWrapper w = (WhereExpressionWrapper) node;
                queryCriteria.addWhereExpression(w.getWhereExpression());
            } else {
                badNodes.add(node);
            }
        }

        if (openCount != closeCount) {
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_UNMATCHED_BRACKETS,
                                                  "Bracket counts don't match open:" + openCount + " close:" + closeCount);
        }
        if (badNodes.size() > 0) {
            logger.error("Bad query syntax. Unknown tokens: " + badNodes);
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_WHERE);
        }
    }

    private void logList(String label, List<ParsingQueryNode> nodes) {
        logger.debug("start: " + label);
        for (ParsingQueryNode n : nodes) {
            logger.debug(n.toString() + " wasProcessed:" + n.isWasProcessed());
        }
        logger.debug("end: " + label);
    }

    /*
     * Convert a list of (1, 2, 3) to a ParsingQueryNodeListToken
     */
    private List<ParsingQueryNode> convertInLists(List<ParsingQueryNode> nodes) throws ApplicationRuntimeException {


        boolean stillProcessing = true;
        while (stillProcessing) {

            for (int i = 0; i < nodes.size(); i++) {
                stillProcessing = false;

                ParsingQueryNode node = nodes.get(i);
                if (node.isWasProcessed())
                    continue;

                if (node instanceof OpenBracketToken) {

                    int j = i + 1;
                    if (j >= nodes.size())
                        throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_UNMATCHED_BRACKETS,
                                                              "Mismatched open bracket");

                    ParsingQueryNode nextNode = nodes.get(j);

                    if (nextNode instanceof NumberToken
                            || nextNode instanceof StringToken
                            || nextNode instanceof BooleanToken
                            || nextNode instanceof IncompleteToken) {
                        stillProcessing = true;
                        int closeBracketLocation = -1;

                        for (j = i; j < nodes.size(); j++) {
                            ParsingQueryNode searchNode = nodes.get(j);
                            if (searchNode instanceof CloseBracketToken) {
                                closeBracketLocation = j;
                                break;
                            }
                        }
                        if (closeBracketLocation == -1)
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_UNMATCHED_BRACKETS,
                                                                  "Mismatched open bracket - no corresponding close bracket");
                        nodes.add(i, extractList(i, closeBracketLocation, nodes));
                    }
                }
            }
        }
        return filterOutSelected(nodes);
    }

    private QueryInListToken extractList(int start, int end, List<ParsingQueryNode> nodes) {
        QueryInListToken listToken = new QueryInListToken();
        for (int i = start; i <= end; i++) {
            ParsingQueryNode node = nodes.get(i);
            node.setWasProcessed(true);
            if (node instanceof NumberToken || node instanceof StringToken | node instanceof BooleanToken || node instanceof IncompleteToken)
                listToken.addInParameter(node);
        }
        return listToken;
    }

    private List<ParsingQueryNode> convertPrimitiveOperators(List<ParsingQueryNode> nodes)
            throws ApplicationRuntimeException {

        for (int i = 0; i < nodes.size(); i++) {
            ParsingQueryNode node = nodes.get(i);

            if (node.isWasProcessed())
                continue;

            if (node instanceof IsToken) {

                if ((i + 1) >= nodes.size()) {

                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                          "IS must be followed by NULL or NOT");

                } else {

                    ParsingQueryNode nextNode = nodes.get((i + 1));

                    if (nextNode instanceof AssignmentToken) {
                        AssignmentOperator notNullOperator = ((AssignmentToken) nextNode).getOperator();
                        if (notNullOperator != AssignmentOperator.NOT)
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "IS must be followed by NULL or NOT");
                        nextNode.setWasProcessed(true);

                        if ((i + 2) >= nodes.size()) {
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "IS NOT must be followed by a NULL");
                        } else {
                            // Check for NULL
                            ParsingQueryNode nextNextNode = nodes.get((i + 2));
                            if (!(nextNextNode instanceof NullToken))
                                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                      "IS NOT must be followed by a NULL");
                            nextNextNode.setWasProcessed(true);
                            nodes.add(i, new QueryExpressionToken(ExpressionOperator.IS_NOT_NULL));

                        }

                    } else {
                        if (!(nextNode instanceof NullToken))
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "IS must be followed by a NULL or NOT NULL");
                        nextNode.setWasProcessed(true);
                        nodes.add(i, new QueryExpressionToken(ExpressionOperator.IS_NULL));
                    }
                }
                node.setWasProcessed(true);

            } else if (node instanceof AssignmentToken) {
                AssignmentOperator assignmentOperator = ((AssignmentToken) node).getOperator();
                node.setWasProcessed(true);
                if (assignmentOperator == AssignmentOperator.EQUALS) {
                    node.setWasProcessed(true);
                    nodes.add(i, new QueryExpressionToken(ExpressionOperator.EQUALS));
                } else {
                    if ((i + 1) >= nodes.size()) {
                        throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                              "NOT not followed by '=', 'IN' or 'LIKE' ");
                    }
                    ParsingQueryNode nextNode = nodes.get((i + 1));
                    nextNode.setWasProcessed(true);
                    if (!(nextNode instanceof AssignmentToken || nextNode instanceof ComparisonToken))
                        throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                              "NOT not followed by '=', 'IN' or 'LIKE' ");

                    if (nextNode instanceof AssignmentToken) {
                        AssignmentOperator nextOperator = ((AssignmentToken) nextNode).getOperator();
                        if (nextOperator == AssignmentOperator.EQUALS) {
                            nextNode.setWasProcessed(true);
                            nodes.add(i, new QueryExpressionToken(ExpressionOperator.NOT_EQUALS));
                        } else {
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "NOT not followed by '=', 'IN' or LIKE");
                        }
                    } else if (nextNode instanceof LikeToken) {
                        nextNode.setWasProcessed(true);
                        nodes.add(i, new QueryExpressionToken(ExpressionOperator.NOT_LIKE));
                    } else if (nextNode instanceof QueryExpressionToken) {
                        QueryExpressionToken queryExpressionToken = (QueryExpressionToken) nextNode;
                        ExpressionOperator operator = queryExpressionToken.getOperator();
                        if (operator == ExpressionOperator.IN) {
                            nextNode.setWasProcessed(true);
                            nodes.add(i, new QueryExpressionToken(ExpressionOperator.NOT_IN));
                        } else {
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "NOT not followed by '=', 'IN' or LIKE");
                        }
                    } else {
                        throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                              "NOT not followed by '=', 'IN' or LIKE");
                    }
                }
            } else if (node instanceof LikeToken) {
                node.setWasProcessed(true);
                nodes.add(i, new QueryExpressionToken(ExpressionOperator.LIKE));
            } else if (node instanceof ComparisonToken) {
                ComparisonOperator comparisonOperator = ((ComparisonToken) node).getComparisonOperator();
                boolean isEqualsAsWell = false;

                if ((i + 1) < nodes.size()) {
                    ParsingQueryNode nextNode = nodes.get((i + 1));
                    if (nextNode instanceof AssignmentToken) {
                        AssignmentOperator nextOperator = ((AssignmentToken) nextNode).getOperator();
                        if (nextOperator == AssignmentOperator.EQUALS) {
                            nextNode.setWasProcessed(true);
                            isEqualsAsWell = true;
                        } else {
                            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                                  "NOT not legal after comparison operators.");
                        }
                    }
                }
                node.setWasProcessed(true);
                nodes.add(i, new QueryExpressionToken(comparisonOperator, isEqualsAsWell));
            }
        }

        return filterOutSelected(nodes);
    }

    private List<ParsingQueryNode> filterOutSelected(List<ParsingQueryNode> nodesIn) {
        ArrayList<ParsingQueryNode> nodesOut = new ArrayList<>();
        for (ParsingQueryNode n : nodesIn) {
            if (!n.isWasProcessed())
                nodesOut.add(n);
        }
        return nodesOut;
    }

    private List<ParsingQueryNode> convertWhereExpressions(List<ParsingQueryNode> nodes) throws ApplicationRuntimeException {

        boolean stillProcessing = true;
        while (stillProcessing) {
            for (int i = 0; i < nodes.size(); i++) {
                ParsingQueryNode node = nodes.get(i);
                stillProcessing = false;
                if (node.isWasProcessed())
                    continue;

                if (node instanceof QueryPropertyToken) {
                    nodes.add(
                            i,
                            convertToWhereExpression(i, nodes));
                    stillProcessing = true;
                }
            }
        }
        return filterOutSelected(nodes);
    }

    private List<ParsingQueryNode> convertWhereExpressionsContainingWithinOperators(List<ParsingQueryNode> nodes) throws ApplicationRuntimeException {

        boolean stillProcessing = true;
        while (stillProcessing) {
            for (int i = 0; i < nodes.size(); i++) {
                ParsingQueryNode node = nodes.get(i);
                stillProcessing = false;
                if (node.isWasProcessed())
                    continue;

                if (node instanceof WhereExpressionWrapper) {
                    WhereExpressionWrapper whereExpressionWrapper = (WhereExpressionWrapper) node;
                    if (whereExpressionWrapper.getWhereExpression().getOperator() == ExpressionOperator.WITHINMTH ||
                            whereExpressionWrapper.getWhereExpression().getOperator() == ExpressionOperator.WITHINYEAR) {
                        nodes.add(
                                i,
                                new CloseBracketToken());
                        nodes.add(
                                i,
                                convertWithinEndWhereExpression(whereExpressionWrapper));
                        nodes.add(
                                i,
                                new ExpressionConnectorWrapper(ExpressionConnector.AND));
                        nodes.add(
                                i,
                                convertWithinStartWhereExpression(whereExpressionWrapper));
                        nodes.add(
                                i,
                                new OpenBracketToken());
                        stillProcessing = true;
                        node.setWasProcessed(true);
                    }
                }
            }
        }
        return filterOutSelected(nodes);
    }

    private WhereExpressionWrapper convertToWhereExpression(
            int i,
            List<ParsingQueryNode> nodes) throws ApplicationRuntimeException {

        QueryPropertyToken queryPropertyToken = (QueryPropertyToken) nodes.get(i);

        if (queryPropertyToken.getProperty() == null)
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PROPERTY);
        queryPropertyToken.setWasProcessed(true);

        QueryProperty queryProperty = queryPropertyToken.getProperty();

        ParsingQueryNode nextNode = nodes.get((i + 1));
        if (!(nextNode instanceof QueryExpressionToken))
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX);
        QueryExpressionToken expressionToken = (QueryExpressionToken) nodes.get((i + 1));
        expressionToken.setWasProcessed(true);

        Object parameter = null;

        if (!expressionToken.getOperator().noParmetersRequired()) {
            if ((i + 2) >= nodes.size())
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PARAM,
                                                      "Missing parameter for query operator ");

            ParsingQueryNode paramNode = nodes.get((i + 2));
            paramNode.setWasProcessed(true);

            if (expressionToken.getOperator().parametersAreInList()) {
                if (!(paramNode instanceof QueryInListToken))
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PARAM,
                                                          "Parameters for 'IN' must be in a list");
                parameter = convertParmsToCorrectType(
                        (QueryInListToken) paramNode,
                        queryProperty);
            } else {
                parameter = convertParamerToCorrectType(paramNode, queryProperty);
            }
        }

        WhereExpression whereExpression = lowerCaseLikeExpression(
                queryPropertyToken.getAlias(),
                queryProperty,
                expressionToken.getOperator(),
                parameter);

        return new WhereExpressionWrapper(whereExpression);

    }

    private WhereExpressionWrapper convertWithinStartWhereExpression(WhereExpressionWrapper originalExpression) {
        ExpressionOperator newOperator = ExpressionOperator.GREATER_THAN_OR_EQUAL_TO;
        Object value = originalExpression.getWhereExpression().getPropertyValue();
        LocalDate convertedDate = (LocalDate) value;
        if (originalExpression.getWhereExpression().getOperator() == ExpressionOperator.WITHINMTH) {
            convertedDate = convertedDate.with(TemporalAdjusters.firstDayOfMonth());
        } else {
            convertedDate = convertedDate.with(TemporalAdjusters.firstDayOfYear());
        }
        WhereExpression convertedExpression = new WhereExpression(
                originalExpression.getWhereExpression().getQueryProperty(),
                newOperator,
                convertedDate);

        return new WhereExpressionWrapper(convertedExpression);
    }


    private WhereExpressionWrapper convertWithinEndWhereExpression(WhereExpressionWrapper originalExpression) {
        ExpressionOperator newOperator = ExpressionOperator.LESS_THAN_OR_EQUAL_TO;
        Object value = originalExpression.getWhereExpression().getPropertyValue();
        LocalDate convertedDate = (LocalDate) value;
        if (originalExpression.getWhereExpression().getOperator() == ExpressionOperator.WITHINMTH) {
            convertedDate = convertedDate.with(TemporalAdjusters.lastDayOfMonth());
        } else {
            convertedDate = convertedDate.with(TemporalAdjusters.lastDayOfYear());
        }
        WhereExpression convertedExpression = new WhereExpression(
                originalExpression.getWhereExpression().getQueryProperty(),
                newOperator,
                convertedDate);

        return new WhereExpressionWrapper(convertedExpression);
    }


    private WhereExpression lowerCaseLikeExpression(
            String alias,
            QueryProperty queryProperty,
            ExpressionOperator operator,
            Object parameter) {

        if (operator == ExpressionOperator.CONTAINS || operator == ExpressionOperator.LIKE || operator == ExpressionOperator.START_LIKE) {
            String lowerCasedValue = ((String) parameter).toLowerCase();
            WhereExpression whereExpression;
            if (alias != null) {
                whereExpression = new WhereExpression(
                        alias,
                        queryProperty,
                        operator,
                        lowerCasedValue);
            } else {
                whereExpression = new WhereExpression(
                        queryProperty,
                        operator,
                        lowerCasedValue);
            }

            whereExpression.setIgnoreCaseFlag(true);
            return whereExpression;
        } else {
            if (alias != null) {
                return new WhereExpression(
                        alias,
                        queryProperty,
                        operator,
                        parameter);
            } else {
                return new WhereExpression(
                        queryProperty,
                        operator,
                        parameter);
            }
        }
    }

    private Object convertParmsToCorrectType(
            QueryInListToken listToken,
            QueryProperty queryProperty) throws ApplicationRuntimeException {

        ArrayList<Object> parmList = new ArrayList<Object>();
        for (ParsingQueryNode n : listToken.getList()) {
            parmList.add(
                    queryProperty.convertValue(
                            convertParamerToCorrectType(
                                    n,
                                    queryProperty)));
        }
        return parmList;
    }

    private Object convertParamerToCorrectType(
            ParsingQueryNode parmIn,
            QueryProperty queryProperty) throws ApplicationRuntimeException {

        if (!(parmIn instanceof ParsingFormulaParameter))
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_INVALID_PARAMETER,
                                                  "Unknown value " + parmIn.toString());

        ParsingFormulaParameter holder = (ParsingFormulaParameter) parmIn;
        return queryProperty.convertValue(holder.getValue());
    }

}
