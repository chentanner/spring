package com.sss.app.core.query.expressions;

import com.sss.app.core.query.parsing.QueryNode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum JoinType implements QueryNode {

    JOIN("JOIN", "JOIN"),
    LEFT_OUTER_JOIN("LEFT JOIN", "OUTER JOIN");

    private JoinType(String type, String externalType) {
        this.type = type;
        this.externalType = externalType;
    }

    private String type;
    private String externalType;

    private static final Map<String, JoinType> lookup
            = new HashMap<String, JoinType>();

    static {
        for (JoinType o : EnumSet.allOf(JoinType.class))
            lookup.put(o.externalType, o);

    }

    public boolean parametersAreInList() {
        return type.equals("L");
    }

    public boolean noParametersRequired() {
        return type.equals("N");
    }

    public String getType() {
        return type;
    }

    public String getExternalType() {
        return externalType;
    }

    public static JoinType lookup(String value) {
        return lookup.get(value);
    }

}