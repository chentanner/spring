package com.sss.app.core.query.parsing.token;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SquareBracket {
    OPEN('['),
    CLOSE(']');

    private final Character code;
    private final Integer unicodeInt;

    private static final Map<Character, SquareBracket> lookup = new HashMap<>();

    private static final Map<Integer, SquareBracket> lookupUnicodeInt = new HashMap<>();


    static {
        for (SquareBracket o : EnumSet.allOf(SquareBracket.class))
            lookup.put(o.code, o);
        for (SquareBracket o : EnumSet.allOf(SquareBracket.class))
            lookupUnicodeInt.put(o.unicodeInt, o);
    }

    public static boolean isBracket(char code) {
        SquareBracket b = lookup.get(code);
        return b != null;
    }

    public static boolean isOpenBracket(char code) {
        SquareBracket b = lookup.get(code);
        if (b == null)
            return false;
        return b == SquareBracket.OPEN;
    }

    public static boolean isCloseBracket(char code) {
        SquareBracket b = lookup.get(code);
        if (b == null)
            return false;
        return b == SquareBracket.CLOSE;
    }

    public static SquareBracket lookup(char code) {
        return lookup.get(code);
    }

    private SquareBracket(char opCode) {
        this.code = opCode;
        this.unicodeInt = (int) code;
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