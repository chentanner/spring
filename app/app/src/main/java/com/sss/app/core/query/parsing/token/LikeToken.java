package com.sss.app.core.query.parsing.token;

public class LikeToken extends AbstractOperatorToken {

    public static boolean isEqualTo(String value) {
        return ("LIKE".equalsIgnoreCase(value));
    }
}