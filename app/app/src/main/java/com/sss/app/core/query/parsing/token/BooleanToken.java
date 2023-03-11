package com.sss.app.core.query.parsing.token;

public class BooleanToken extends AbstractValueToken {

    private final Boolean bool;

    public BooleanToken(String boolAsString) {
        bool = new Boolean(boolAsString);
    }

    public BooleanToken(Boolean bool) {
        super();
        this.bool = bool;
    }

    public Boolean getValue() {
        return bool;
    }
}