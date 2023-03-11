package com.sss.app.core.query.parsing.token;

public class NullToken extends AbstractQueryNode {
    public static boolean isEqualTo(String value) {
        return ("NULL".equalsIgnoreCase(value));
    }
}