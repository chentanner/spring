package com.sss.app.core.enums;

import java.util.*;

public enum TransactionErrorCode {
    SUCCESS("0"),
    ERROR("APP-000000"),
    JSON_CODE_ITEM_SERIALIZATION("APP-009000"),

    HIBERNATE_SESSION("APP-010000"),

    MORE_THAN_ONE_OBJECT_RETURNED_QUERY("APP-010100"),
    STALE_UPDATE_VERSION_FAIL("APP-010101"),
    ILLEGAL_UPDATE_EXPIRED_ENTITY("APP-010102"),
    DELETE_FAILED("APP-010103"),
    NULL_ENTITY_ENCOUNTERED("APP-010104"),
    BAD_ENTITY_ID("APP-010105"),
    BAD_BUSINESS_KEY("APP-010106"),


    STATELESS_SESSION_CREATE_FAILED_EXISTS("APP-010200"),

    WEB_REST_CLIENT_FAILED("APP-010300"),
    MISSING_BUSINESS_KEY("APP-100100"),
    MISSING_CODE("APP-1001500"),
    ;
    private final String code;

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
        ArrayList<String> list = new ArrayList<>();
        for (TransactionErrorCode c : EnumSet.allOf(TransactionErrorCode.class))
            list.add(c.getCode());
        return list;
    }

    public static TransactionErrorCode lookUp(String code) {
        return lookup.get(code);
    }
}
