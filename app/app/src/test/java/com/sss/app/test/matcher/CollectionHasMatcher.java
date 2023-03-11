package com.sss.app.test.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionHasMatcher<R extends BaseMatcher<T>, T> extends BaseMatcher<Collection<T>> {

    private final List<R> expectedMatchers = new ArrayList<>();

    public CollectionHasMatcher() {
    }

    public CollectionHasMatcher<R, T> has(R matcher) {
        if (matcher != null)
            expectedMatchers.add(matcher);
        return this;
    }

    @Override
    public void matches(Collection<T> matchingCollection) {
        if (matchingCollection == null) {
            throwException("Cannot matching against a null collection");
        }

        for (R matcher : expectedMatchers) {
            T found = null;
            for (T matchable : matchingCollection) {
                try {
                    matcher.matches(matchable);
                    found = matchable;
                    break;
                } catch (Throwable ignored) {
                }
            }
            if (found == null) {
                throwException(matchingCollection, matcher);
            }
        }
    }

    private void throwException(String msg) {
        throw new AssertionError(msg);
    }

    private void throwException(Collection<T> matchingCollection, R failedMatcher) {
        String error = "Expected to find an instance of " + failedMatcher.getMatchedClassName() + ": " +
                System.getProperty("line.separator") +
                failedMatcher.getMatchedObject() +
                System.getProperty("line.separator") +
                "In the collection " + matchingCollection.getClass().getName() + ":" +
                System.getProperty("line.separator") +
                matchingCollection;

        throw new AssertionError(error);
    }

    @Override
    public String toString() {
        return expectedMatchers.toString();
    }
}
