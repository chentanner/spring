package com.sss.app.core.query.expressions;

import com.sss.app.core.query.parsing.token.AbstractQueryNode;

public class ExpressionConnectorWrapper extends AbstractQueryNode {

    private final ExpressionConnector connector;

    public ExpressionConnectorWrapper(ExpressionConnector connector) {
        super();
        this.connector = connector;
    }

    public ExpressionConnector getConnector() {
        return connector;
    }

    public String toString() {
        return connector.getConnector();
    }
}