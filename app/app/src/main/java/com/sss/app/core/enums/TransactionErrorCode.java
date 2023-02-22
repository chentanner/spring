package com.sss.app.core.enums;

import java.util.*;

public enum TransactionErrorCode {
    SUCCESS("0"),
    JSON_CODE_ITEM_SERIALIZATION("APP-009000"),
    HIBERNATE_SESSION("APP-010000"),
    STATELESS_SESSION_CREATE_FAILED_EXISTS("APP-010100"),
    WEB_REST_CLIENT_FAILED("APP-010200");
    private String code;

    private static final Map<String, TransactionErrorCode> lookup
            = new HashMap<String, TransactionErrorCode>();

    static {
        for (TransactionErrorCode c : EnumSet.allOf(TransactionErrorCode.class))
            lookup.put(c.code, c);
    }

    private TransactionErrorCode(String code) {
        this.code = code;
    }

    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public static List<String> getTransactionCodes() {
        ArrayList<String> list = new ArrayList<String>();
        for (TransactionErrorCode c : EnumSet.allOf(TransactionErrorCode.class))
            list.add(c.getCode());
        return list;
    }

    public static TransactionErrorCode lookUp(String code) {
        return lookup.get(code);
    }
}
