package com.sss.app.core.query.parsing.token;

import java.math.BigDecimal;

public class NumberToken extends AbstractValueToken {

    private final BigDecimal number;

    public NumberToken(BigDecimal number) {
        super();
        this.number = number;
    }

    public BigDecimal getValue() {
        return number;
    }

    public String toString() {
        return number.toPlainString();
    }

}