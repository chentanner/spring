package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.operator.ComparisonOperator;

public class ComparisonToken extends AbstractOperatorToken {

    private final ComparisonOperator comparisonOperator;

    public ComparisonToken(ComparisonOperator comparisonOperator) {
        super();
        this.comparisonOperator = comparisonOperator;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
