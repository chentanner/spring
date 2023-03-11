package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.expressions.ExpressionOperator;
import com.sss.app.core.query.parsing.operator.ComparisonOperator;

public class QueryExpressionToken extends AbstractOperatorToken {

    private final ExpressionOperator operator;

    public QueryExpressionToken(ExpressionOperator operator) {
        super();
        this.operator = operator;
    }

    public QueryExpressionToken(ComparisonOperator comparisonOperator, boolean isEquals) {
        super();
        this.operator = convert(comparisonOperator, isEquals);
    }

    public ExpressionOperator getOperator() {
        return operator;
    }

    private ExpressionOperator convert(ComparisonOperator operator, boolean isEquals) {
        if (operator == ComparisonOperator.GREATER_THAN) {
            if (isEquals)
                return ExpressionOperator.GREATER_THAN_OR_EQUAL_TO;
            else
                return ExpressionOperator.GREATER_THAN;
        } else {
            if (isEquals)
                return ExpressionOperator.LESS_THAN_OR_EQUAL_TO;
            else
                return ExpressionOperator.LESS_THAN;
        }
    }

    public String toString() {
        return operator.toString();
    }
}