package com.sss.app.core.query.expressions;

import com.sss.app.core.query.parsing.QueryNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ExpressionOperator implements QueryNode {

    IN("in", "in", "L"),
    NOT_IN("not in", "not in", "L"),
    EQUALS("=", "eq", "s"),
    NOT_EQUALS("!=", "ne", "s"),
    GREATER_THAN(">", "gt", "s"),
    GREATER_THAN_OR_EQUAL_TO(">=", "ge", "s"),
    LESS_THAN("<", "lt", "s"),
    LESS_THAN_OR_EQUAL_TO("<=", "le", "s"),
    IS_NULL(" is null", "is null", "N"),
    IS_NOT_NULL(" is not null", "is not null", "N"),
    LIKE("like", "like", "s"),
    START_LIKE("_like", "startsWith", "s"),
    CONTAINS("contains", "contains", "s"),
    WITHINMTH("withinmth", "withinmth", "s"),
    WITHINYEAR("withinyear", "withinyear", "s"),
    NOT_LIKE("not like", "not like", "s");

    private static final Logger logger = LogManager.getLogger();

    private ExpressionOperator(String operator, String externalOperator, String type) {
        this.operator = operator;
        this.externalOperator = externalOperator;
        this.type = type;

    }

    private final String operator;
    private final String externalOperator;
    private String type = "s";

    private static final Map<String, ExpressionOperator> lookup
            = new HashMap<String, ExpressionOperator>();

    private static final Map<String, ExpressionOperator> lookupExternal
            = new HashMap<String, ExpressionOperator>();

    static {
        for (ExpressionOperator o : EnumSet.allOf(ExpressionOperator.class))
            lookup.put(o.operator, o);

        for (ExpressionOperator o : EnumSet.allOf(ExpressionOperator.class))
            lookupExternal.put(o.externalOperator, o);
    }

    public boolean parametersAreInList() {
        return type.equals("L");
    }

    public boolean noParmetersRequired() {
        return type.equals("N");
    }

    public String getOperator() {
        return operator;
    }

    public static ExpressionOperator lookup(String name) {
        String value = name.toLowerCase();
        return lookup.get(value);
    }

    public static ExpressionOperator lookupByExternalOperator(String name) {

        if (name.equalsIgnoreCase("startsWith"))
            return START_LIKE;


        String value = name.toLowerCase();
        return lookupExternal.get(value);
    }

    public String getExternalOperator() {
        return externalOperator;
    }

    public static List<String> getFilteredSortedOperatorList(String filter) {

        return lookupExternal.keySet()
                .stream()
                .filter(c -> contains(filter, c))
                .sorted()
                .collect(Collectors.toList());
    }

    private static boolean contains(String prefix, String externalOperator) {
        if (externalOperator.length() < prefix.length())
            return false;

        int length = prefix.length();
        String compareStr = externalOperator.substring(0, length);
        return prefix.equalsIgnoreCase(compareStr);

    }

}