package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.expressions.QueryProperty;
import com.sss.app.core.query.parsing.ParsingQueryNode;

public class QueryPropertyToken extends AbstractQueryNode implements ParsingQueryNode {

    private QueryProperty property;
    private String alias;
    private String partialName;

    public QueryPropertyToken(QueryProperty property) {
        super();
        this.property = property;
    }

    public QueryPropertyToken(QueryProperty property, String alias) {
        super();
        this.property = property;
        this.alias = alias;
    }

    public QueryPropertyToken(String alias, String partialName) {
        this.alias = alias;
        this.partialName = partialName;
    }

    public String getPartialName() {
        return partialName;
    }

    public String getAlias() {
        return alias;
    }

    public QueryProperty getProperty() {
        return property;
    }

    public String toString() {
        if (alias == null) {
            if (property != null)
                return property.getLabel();
            return "";
        } else {
            if (property != null)
                return alias + "." + property.getLabel();
            else
                return alias + ".";
        }
    }
}