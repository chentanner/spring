package com.sss.app.core.query.parsing;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Bracket implements QueryNode {
    OPEN('('),
    CLOSE(')');

    private final Character code;
    private final Integer unicodeInt;

    private static final Map<Character, Bracket> lookup
            = new HashMap<>();

    private static final Map<Integer, Bracket> lookupUnicodeInt
            = new HashMap<>();


    static {
        for (Bracket o : EnumSet.allOf(Bracket.class))
            lookup.put(o.code, o);
        for (Bracket o : EnumSet.allOf(Bracket.class))
            lookupUnicodeInt.put(o.unicodeInt, o);
    }

    public static Bracket lookup(char code) {
        return lookup.get(code);
    }

    public static Bracket lookup(String s) {
        if (s == null || s.isEmpty())
            return null;

        char c = s.charAt(0);
        return lookup(c);
    }

    private Bracket(char opCode) {
        this.code = opCode;
        this.unicodeInt = (int) opCode;
    }

    public Character getCode() {
        return code;
    }

    public Integer getUnicodeInt() {
        return unicodeInt;
    }

    public String toString() {
        return code.toString();
    }


}