package com.sss.app.core.query.parsing;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.query.expressions.*;
import com.sss.app.core.query.parsing.operator.AssignmentOperator;
import com.sss.app.core.query.parsing.operator.ComparisonOperator;
import com.sss.app.core.query.parsing.token.*;
import com.sss.app.core.query.propertymapper.QueryPropertyMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryAsTextReader extends BaseTextParser {
    private static final Logger logger = LogManager.getLogger(QueryAsTextReader.class);
    private static final String DEFAULT_LIKE_OPERATOR = ";;";

    private final List<ParsingQueryNode> tokenList = new ArrayList<>();
    private final String queryEntityName;
    private final String expressionText;
    private Map<String, String> joinAlias;

    public QueryAsTextReader(
            String queryEntityName,
            String expressionTextIn) {

        this.queryEntityName = queryEntityName;
        expressionText = convertLikeOperatorAlias(expressionTextIn);
    }

    public QueryAsTextReader(
            Map<String, String> joinAlias,
            String queryEntityName,
            String expressionTextIn) {

        this.joinAlias = joinAlias;
        this.queryEntityName = queryEntityName;
        expressionText = convertLikeOperatorAlias(expressionTextIn);
    }

    public void parse() {
        validateBracketCount(expressionText);
        validateSingleQuoteCount(expressionText);
        parseToNodes();
    }

    private void validateSingleQuoteCount(String expressionText) {
        int count = StringUtils.countOccurrencesOf(expressionText, "'");
        int rem = count % 2;
        boolean areSingleQuoteMatched = (rem == 0);
        if (!areSingleQuoteMatched)
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_BAD_SYNTAX,
                                                  "Unmatched Single Quotes: " + count);
    }

    public void partialParse() {
        parseToNodes();
    }

    public List<ParsingQueryNode> getTokenList() {
        return tokenList;
    }

    private String convertLikeOperatorAlias(String text) {
        return text.replaceAll(DEFAULT_LIKE_OPERATOR, "%");
    }

    private void parseToNodes() {

        int count = StringUtils.countOccurrencesOf(expressionText, "'");
        int rem = count % 2;
        boolean areSingleQuoteMatched = (rem == 0);

        try {

            BasicTextReader reader = new BasicTextReader(expressionText);

            while (reader.hasNext()) {

                switch (reader.getNextType()) {

                    case BasicTextReader.IS_WHITESPACE:
                        reader.read();
                        break;

                    case BasicTextReader.IS_BRACKET:
                        tokenList.add(createBracketToken(reader.read()));
                        break;

                    case BasicTextReader.IS_ASSIGNMENT_OPERATOR:
                        tokenList.add(
                                createAssignmentOperator(reader.read()));
                        break;

                    case BasicTextReader.IS_COMPARISON_OPERATOR:
                        tokenList.add(
                                createComparisonOperator(reader.read()));
                        break;

                    case BasicTextReader.IS_BANG:
                        reader.read();
                        tokenList.add(
                                new AssignmentToken(AssignmentOperator.NOT));
                        break;

                    case BasicTextReader.IS_SCALER:
                        tokenList.add(
                                createNumberToken(reader.readDigits()));
                        break;


                    case BasicTextReader.IS_SEPARATOR:
                        reader.read();
                        tokenList.add(createSeparatorToken());
                        break;

                    case BasicTextReader.IS_LETTER:

                        tokenList.add(
                                processString(
                                        reader.readStringToken()));
                        break;

                    case BasicTextReader.IS_QUOTE:
                        String value = reader.readExtendedQuotedToken();
                        StringToken token = new StringToken(value);
                        token.setQuoteMatched(areSingleQuoteMatched);
                        tokenList.add(token);
                        break;

                    default:
                        reader.read();
                }
            }

        } catch (RuntimeException t) {
            logger.error("Unknown exception thrown while parsing query.", t);
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_QUERY_FAILED, t);
        }
    }

    private NumberToken createNumberToken(String digits) {
        BigDecimal value = new BigDecimal(digits);
        return new NumberToken(value);
    }

    private SeparatorToken createSeparatorToken() {
        return new SeparatorToken();
    }

    private AssignmentToken createAssignmentOperator(char s) {
        return new AssignmentToken(AssignmentOperator.lookup(s));
    }

    private ComparisonToken createComparisonOperator(char s) {
        return new ComparisonToken(ComparisonOperator.lookup(s));
    }

    private ParsingQueryNode processString(String name) {

        QueryProperty queryProperty;

        // Has alias
        if (name.contains(".")) {
            String alias = null;
            String propertyName;
            String[] parts = name.split("\\.");
            if (parts.length == 1) {
                alias = parts[0];
                propertyName = "";
            } else {
                alias = parts[0];
                propertyName = parts[1];
                if (alias.isBlank())
                    throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_ENTITY_NAMES_MISMATCH);
            }

            if (alias != null && joinAlias == null)
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_ENTITY_NAMES_MISMATCH);

            String joinQueryEntityName = joinAlias.get(alias);
            if (!propertyName.isEmpty()) {
                queryProperty = getQueryPropertyMapper().getQueryMap(joinQueryEntityName).get(propertyName);
                if (queryProperty != null)
                    return new QueryPropertyToken(queryProperty, alias);
                else
                    return new QueryPropertyToken(alias, propertyName);
            } else {
                return new QueryPropertyToken(alias, propertyName);
            }
        } else {

            queryProperty = getQueryPropertyMap().get(name);
            if (queryProperty != null)
                return new QueryPropertyToken(queryProperty);
            else
                return processStringToToken(name);
        }

    }

    private ParsingQueryNode processStringToToken(String name) {

        if ("true".equalsIgnoreCase(name) || "false".equalsIgnoreCase(name)) {
            return new BooleanToken(name);
        }

        if ("not".equalsIgnoreCase(name)) {
            return new AssignmentToken(AssignmentOperator.NOT);
        }

        if (LikeToken.isEqualTo(name)) {
            return new LikeToken();
        }

        if (IsToken.isEqualTo(name)) {
            return new IsToken();
        }

        if (NullToken.isEqualTo(name)) {
            return new NullToken();
        }

        OrderOperator orderOperator = OrderOperator.lookup(name);
        if (orderOperator != null)
            return new OrderOperatorToken(orderOperator);

        ExpressionOperator operator = ExpressionOperator.lookup(name);
        if (operator != null) {
            return new QueryExpressionToken(operator);
        }

        operator = ExpressionOperator.lookupByExternalOperator(name);
        if (operator != null) {
            return new QueryExpressionToken(operator);
        }

        ExpressionConnector connector = ExpressionConnector.lookup(name);
        if (connector != null) {
            return new ExpressionConnectorWrapper(connector);
        }

        return new IncompleteToken(name);

    }

    private Map<String, QueryProperty> getQueryPropertyMap() {
        QueryPropertyMapper queryPropertyMapper = getQueryPropertyMapper();
        Map<String, QueryProperty> mapper = queryPropertyMapper.getQueryMap(queryEntityName);
        if (mapper == null) {
            logger.error("Invalid queryEntityName: " + queryEntityName);
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_TEXT_MISSING_PROPERTY,
                                                  queryEntityName);
        }
        return mapper;
    }

    private QueryPropertyMapper getQueryPropertyMapper() {
        return ApplicationContextFactory.getBean(QueryPropertyMapper.class);
    }


    private ParsingQueryNode createBracketToken(char token) {
        Bracket bracket = Bracket.lookup(token);
        if (bracket.equals(Bracket.OPEN))
            return new OpenBracketToken();
        else
            return new CloseBracketToken();
    }


}
