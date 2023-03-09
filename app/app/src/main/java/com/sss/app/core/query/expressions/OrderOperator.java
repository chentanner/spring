package com.sss.app.core.query.expressions;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum OrderOperator {

    DESCENDING("DESC", "DESCENDING"),
    ASCENDING("ASC", "ASCENDING");

    private OrderOperator(String operator, String name) {
        this.operator = operator;
        this.operatorName = name;
    }

    private static final Map<String, OrderOperator> lookup = new HashMap<>();

    private static final Map<String, OrderOperator> lookupByName = new HashMap<>();

    private final String operator;
    private final String operatorName;

    public String getOperator() {
        return operator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    static {
        for (OrderOperator o : EnumSet.allOf(OrderOperator.class))
            lookup.put(o.operator, o);
        for (OrderOperator o : EnumSet.allOf(OrderOperator.class))
            lookupByName.put(o.operatorName, o);
    }

    public static OrderOperator lookup(String op) {
        OrderOperator operator = lookup.get(op.toUpperCase());
        if (operator != null)
            return operator;

        operator = lookupByName.get(op.toUpperCase());
        return operator;
    }

    public static OrderOperator lookupByName(String op) {
        return lookupByName.get(op.toUpperCase());
    }

}
