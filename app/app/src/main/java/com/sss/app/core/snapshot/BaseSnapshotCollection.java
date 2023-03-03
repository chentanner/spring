package com.sss.app.core.snapshot;

import com.sss.app.core.entity.snapshot.ErrorResponse;

import java.util.List;

public abstract class BaseSnapshotCollection<T> extends AbstractJsonCollection<T> {

    public BaseSnapshotCollection() {
    }

    public BaseSnapshotCollection(WarningSnapshot warning) {
        super(warning);
    }

    public BaseSnapshotCollection(ErrorResponse error) {
        super(error);
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
}
