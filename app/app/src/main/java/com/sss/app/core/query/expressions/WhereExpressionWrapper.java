package com.sss.app.core.query.expressions;

import com.sss.app.core.query.parsing.token.AbstractQueryNode;

public class WhereExpressionWrapper extends AbstractQueryNode {
    private final WhereExpression whereExpression;

    public WhereExpressionWrapper(WhereExpression whereExpression) {
        super();
        this.whereExpression = whereExpression;
    }

    public WhereExpression getWhereExpression() {
        return whereExpression;
    }
}