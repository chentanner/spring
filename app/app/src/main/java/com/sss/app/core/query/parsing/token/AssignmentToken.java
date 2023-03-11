package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.operator.AssignmentOperator;

public class AssignmentToken extends AbstractOperatorToken {

    private final AssignmentOperator operator;

    public AssignmentOperator getOperator() {
        return operator;
    }

    public AssignmentToken(AssignmentOperator operator) {
        super();
        this.operator = operator;
    }

    public String toString() {
        return operator.toString();
    }
}