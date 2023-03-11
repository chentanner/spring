package com.sss.app.core.query.parsing.operator;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Operator {
    PLUS('+', 1),
    MINUS('-', 1),
    MULTIPLY('*', 2),
    DIVIDE('/', 2),
    COLON(':', 5),
    COMMA(',', 6);

    private final Character opCode;
    private final Integer unicodeInt;
    private final int precedence;

    private static final Map<Character, Operator> lookup = new HashMap<>();

    private static final Map<Integer, Operator> lookupUnicodeInt = new HashMap<>();

    static {
        for (Operator o : EnumSet.allOf(Operator.class))
            lookup.put(o.opCode, o);
        for (Operator o : EnumSet.allOf(Operator.class))
            lookupUnicodeInt.put(o.unicodeInt, o);
    }

    public static Operator lookup(char code) {
        return lookup.get(code);
    }

    private Operator(char opCode, int precedence) {
        this.opCode = opCode;
        unicodeInt = (int) opCode;
        this.precedence = precedence;
    }

    public Integer getUnicodeInt() {
        return unicodeInt;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Character getOpCode() {
        return opCode;
    }

    public boolean hasHigherPrecedence(Operator op) {
        return this.precedence > op.precedence;
    }

    public String toString() {
        return opCode.toString();
    }
}
