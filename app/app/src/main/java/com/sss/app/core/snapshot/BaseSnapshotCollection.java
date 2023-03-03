package com.sss.app.core.snapshot;

import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;

import java.util.List;

public abstract class BaseSnapshotCollection<T> extends AbstractJsonCollection<T> {

    private AbstractErrorSnapshot error;
    private WarningSnapshot warning;

    public BaseSnapshotCollection() {
    }

    public BaseSnapshotCollection(AbstractErrorSnapshot error) {
        this.error = error;
    }

    public BaseSnapshotCollection(
            long start,
            long totalItems,
            int limit,
            List<T> snapshots) {
        this(start, totalItems, limit, snapshots, null);
    }

    public BaseSnapshotCollection(
            long start,
            long totalItems,
            int limit,
            List<T> snapshots,
            WarningSnapshot warning) {
        super(start, totalItems, limit, snapshots);
        this.warning = warning;
    }

    public AbstractErrorSnapshot getError() {
        return error;
    }

    public void setError(AbstractErrorSnapshot error) {
        this.error = error;
    }

    public boolean hasErrors() {
        return error != null;
    }

    public WarningSnapshot getWarning() {
        return warning;
    }

    public void setWarning(WarningSnapshot warning) {
        this.warning = warning;
    }

    public boolean hasWarnings() {
        return warning != null;
    }
}
