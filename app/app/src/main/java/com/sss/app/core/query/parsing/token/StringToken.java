package com.sss.app.core.query.parsing.token;

public class StringToken extends AbstractValueToken {

    private final String name;
    private boolean isQuoteMatched = true;

    public StringToken(String name) {
        super();
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean isSingleQuoteOnly() {
        return !isQuoteMatched;
    }

    public boolean isQuoteMatched() {
        return isQuoteMatched;
    }

    public void setQuoteMatched(boolean isQuoteMatched) {
        this.isQuoteMatched = isQuoteMatched;
    }

}
