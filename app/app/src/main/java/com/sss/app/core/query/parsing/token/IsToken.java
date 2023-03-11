package com.sss.app.core.query.parsing.token;

public class IsToken extends AbstractOperatorToken {
    public static boolean isEqualTo(String value) {
        return ("IS".equalsIgnoreCase(value));
    }
}