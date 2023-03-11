package com.sss.app.core.query.parsing.operator;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ComparisonOperator {
    GREATER_THAN('>'),
    LESS_THAN('<');

    private final Character code;

    private static final Map<Character, ComparisonOperator> lookup = new HashMap<>();


    static {
        for (ComparisonOperator o : EnumSet.allOf(ComparisonOperator.class))
            lookup.put(o.code, o);
    }

    public static ComparisonOperator lookup(char code) {
        return lookup.get(code);
    }

    private ComparisonOperator(Character opCode) {
        this.code = opCode;
    }

    public Character getCode() {
        return code;
    }

    public String toString() {
        return code.toString();
    }
}
