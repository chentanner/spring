package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.Bracket;

public class CloseBracketToken extends AbstractQueryNode {
    private Bracket bracket = Bracket.CLOSE;

    public CloseBracketToken() {
    }

    public void parse() {
    }

    public String toString() {
        return bracket.toString();
    }

    public Bracket getBracket() {
        return bracket;
    }

}