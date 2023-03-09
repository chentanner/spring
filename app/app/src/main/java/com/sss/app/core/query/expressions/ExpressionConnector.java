package com.sss.app.core.query.expressions;

import com.sss.app.core.query.parsing.QueryNode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ExpressionConnector implements QueryNode {
    AND("AND"),
    OR("OR");

    private ExpressionConnector(String connector) {
        this.connector = connector;
    }

    private static final Map<String, ExpressionConnector> lookup
            = new HashMap<String, ExpressionConnector>();


    static {
        for (ExpressionConnector o : EnumSet.allOf(ExpressionConnector.class))
            lookup.put(o.connector, o);
    }


    private final String connector;

    public String getConnector() {
        return connector;
    }

    public static ExpressionConnector lookup(String value) {
        return lookup.get(value.toUpperCase());
    }
}
