package com.sss.app.test.matcher;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualsMatcher<T, R> extends BaseMatcher<T> {
    private final Function<T, R> actualValueSupplier;
    private final R expectedValue;
    private String fieldName;

    public EqualsMatcher(Function<T, R> actualValueSupplier, R expectedValue) {
        this.actualValueSupplier = actualValueSupplier;
        this.expectedValue = expectedValue;
    }

    public EqualsMatcher(Function<T, R> actualValueSupplier, R expectedValue, String fieldName) {
        this.actualValueSupplier = actualValueSupplier;
        this.expectedValue = expectedValue;
        this.fieldName = fieldName;
    }

    public void matches(T matchingObject) {
        assertEquals(expectedValue, actualValueSupplier.apply(matchingObject), getFailMessage());
    }

    private String getFailMessage() {
        return fieldName == null
                ? null
                : fieldName + " is not equal";
    }

    public String getMatchedClassName() {
        return expectedValue.getClass().getName();
    }

    public Object getMatchedObject() {
        return expectedValue;
    }

    @Override
    public String toString() {
        return expectedValue != null
                ? expectedValue.toString()
                : null;
    }
}
