package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.ParsingQueryNode;

public class SeparatorToken extends AbstractQueryNode implements ParsingQueryNode {

    private final char value = ',';

    public SeparatorToken() {

    }

    public String toString() {
        return "[comma]";
    }
}