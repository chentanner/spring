package com.sss.app.core.snapshot;

import java.util.List;

public abstract class BaseSnapshotCollection<T> extends AbstractJsonCollection<T> {

    private ErrorSnapshot error;
    private WarningSnapshot warning;

    public BaseSnapshotCollection() {
    }

    public BaseSnapshotCollection(ErrorSnapshot error) {
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

    public ErrorSnapshot getError() {
        return error;
    }

    public void setError(ErrorSnapshot error) {
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
