package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.query.parsing.Bracket;

public class BracketElement extends ExpressionElement {
    private String bracketCode;

    public BracketElement() {
        super("BRACKET");
    }

    public BracketElement(Bracket bracket) {
        super("BRACKET");
        this.bracketCode = bracket.getCode().toString();
    }

    @JsonIgnore
    public Bracket getBracket() {
        return Bracket.lookup(bracketCode);
    }

    public void setBracket(Bracket bracket) {
        this.bracketCode = bracket.getCode().toString();
    }

    public String getBracketCode() {
        return bracketCode;
    }

    public void setBracketCode(String bracket) {
        this.bracketCode = bracket;
    }

    public String toFormattedString() {
        return toString();
    }

    public String toExternalString() {
        return toString();
    }

    public String toString() {
        return bracketCode;
    }
}