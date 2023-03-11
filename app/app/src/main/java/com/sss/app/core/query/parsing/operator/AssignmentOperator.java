package com.sss.app.core.query.parsing.operator;

import com.sss.app.core.query.parsing.QueryNode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AssignmentOperator implements QueryNode {
    EQUALS('='),
    NOT('!');

    private final Character code;

    private static final Map<Character, AssignmentOperator> lookup
            = new HashMap<>();


    static {
        for (AssignmentOperator o : EnumSet.allOf(AssignmentOperator.class))
            lookup.put(o.code, o);
    }

    public static AssignmentOperator lookup(char code) {
        return lookup.get(code);
    }

    private AssignmentOperator(Character opCode) {
        this.code = opCode;
    }

    public Character getCode() {
        return code;
    }

    public String toString() {
        return code.toString();
    }

}