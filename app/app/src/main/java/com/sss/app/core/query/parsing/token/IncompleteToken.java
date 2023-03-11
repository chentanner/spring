package com.sss.app.core.query.parsing.token;

public class IncompleteToken extends AbstractValueToken {

    private final String stringValue;

    public IncompleteToken(String name) {
        super();
        this.stringValue = name;
    }

    public String getValue() {
        return stringValue;
    }

    public String toString() {
        return stringValue;
    }
}