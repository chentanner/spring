package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.expressions.OrderOperator;

public class OrderOperatorToken extends AbstractOperatorToken {
    private final OrderOperator operator;

    public OrderOperatorToken(OrderOperator operator) {
        super();
        this.operator = operator;
    }

    public OrderOperator getOperator() {
        return operator;
    }

    public String toString() {
        return operator.toString();
    }
}