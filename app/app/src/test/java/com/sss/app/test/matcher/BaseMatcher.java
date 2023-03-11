package com.sss.app.test.matcher;

public abstract class BaseMatcher<T> {
    public static <paramType> void safeMatch(BaseMatcher<paramType> matcher, paramType matchTo) {
        if (matcher != null) {
            matcher.matches(matchTo);
        }
    }

    public abstract void matches(T matching);

    public String getMatchedClassName() {
        return getClass().getName();
    }

    public Object getMatchedObject() {
        return this;
    }
}
