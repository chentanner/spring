package com.sss.app.test.matcher;

import java.util.Collection;

public class CollectionMatcher<R extends BaseMatcher<T>, T> extends BaseMatcher<Collection<T>> {


    CollectionHasMatcher<R, T> hasMatcher;
    EqualsMatcher<Collection<T>, Integer> sizeMatcher;

    String collectionName;

    public CollectionMatcher(String collectionFieldName) {
        collectionName = collectionFieldName;
        hasMatcher = new CollectionHasMatcher<>();
    }

    public CollectionMatcher<R, T> has(R matcher) {
        hasMatcher.has(matcher);
        return this;
    }

    public CollectionMatcher<R, T> withSize(Integer size) {
        this.sizeMatcher = new EqualsMatcher<>(Collection::size,
                                               size,
                                               collectionName == null ? null : "Collection size is not equal");
        return this;
    }

    @Override
    public void matches(Collection<T> matchingCollection) {
        BaseMatcher.safeMatch(sizeMatcher, matchingCollection);
        BaseMatcher.safeMatch(hasMatcher, matchingCollection);
    }

    @Override
    public String toString() {
        return "CollectionMatcher{" +
                "size=" + sizeMatcher +
                ", has=" + hasMatcher +
                '}';
    }
}
