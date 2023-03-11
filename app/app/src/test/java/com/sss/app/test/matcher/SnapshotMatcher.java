package com.sss.app.test.matcher;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.core.entity.snapshot.AbstractIdSnapshot;
import com.sss.app.core.entity.snapshot.BusinessKey;

public abstract class SnapshotMatcher<T extends AbstractIdSnapshot, M extends SnapshotMatcher<T, M>> extends BaseMatcher<T> {
    private EqualsMatcher<T, ?> entityIdMatcher;
    private EqualsMatcher<T, ?> entityStateMatcher;
    private EqualsMatcher<T, ?> businessKeyMatcher;

    /**
     * Avoids the unchecked cast warning.
     */
    public abstract M getThis();

    public M withAbstractSnapshot(T expectedSnapshot) {
        return this
                .withEntityState(expectedSnapshot.getEntityState())
                .withEntityId(expectedSnapshot.getEntityId())
                .withBusinessKey(expectedSnapshot.getBusinessKey());
    }

    public M withEntityId(Integer entityId) {
        entityIdMatcher = new EqualsMatcher<>(T::getEntityId, entityId);
        return getThis();
    }

    public M withEntityState(EntityState entityState) {
        entityStateMatcher = new EqualsMatcher<>(T::getEntityState, entityState);
        return getThis();
    }


    public M withBusinessKey(BusinessKey businessKey) {
        businessKeyMatcher = new EqualsMatcher<>(T::getBusinessKey, businessKey);
        return getThis();
    }

    @Override
    public void matches(T snapshot) {
        BaseMatcher.safeMatch(entityIdMatcher, snapshot);
        BaseMatcher.safeMatch(entityStateMatcher, snapshot);
        BaseMatcher.safeMatch(businessKeyMatcher, snapshot);
    }
}
