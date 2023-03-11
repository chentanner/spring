package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.Bracket;

public class OpenBracketToken extends AbstractQueryNode {
    private Bracket bracket = Bracket.OPEN;

    public OpenBracketToken() {
    }


    public String toString() {
        return bracket.toString();
    }


    public Bracket getBracket() {
        return bracket;
    }

}
