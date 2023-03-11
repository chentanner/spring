package com.sss.app.core.query.parsing.token;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CurlyBracket {
    OPEN('{'),
    CLOSE('}');

    private final Character code;
    private final Integer unicodeInt;

    private static final Map<Character, CurlyBracket> lookup = new HashMap<>();

    private static final Map<Integer, CurlyBracket> lookupUnicodeInt = new HashMap<>();

    static {
        for (CurlyBracket o : EnumSet.allOf(CurlyBracket.class))
            lookup.put(o.code, o);
        for (CurlyBracket o : EnumSet.allOf(CurlyBracket.class))
            lookupUnicodeInt.put(o.unicodeInt, o);
    }

    public static boolean isBracket(char code) {
        CurlyBracket b = lookup.get(code);
        return b != null;
    }

    public static boolean isOpenBracket(char code) {
        CurlyBracket b = lookup.get(code);
        if (b == null)
            return false;
        return b == CurlyBracket.OPEN;
    }

    public static boolean isCloseBracket(char code) {
        CurlyBracket b = lookup.get(code);
        if (b == null)
            return false;
        return b == CurlyBracket.CLOSE;
    }

    public static CurlyBracket lookup(char code) {
        return lookup.get(code);
    }

    private CurlyBracket(char opCode) {
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
