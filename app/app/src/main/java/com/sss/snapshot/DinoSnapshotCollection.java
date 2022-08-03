package com.sss.snapshot;

import java.util.List;

public class DinoSnapshotCollection extends BaseSnapshotCollection<DinoSnapshot> {

    public DinoSnapshotCollection() {
    }

    public DinoSnapshotCollection(ErrorSnapshot error) {
        super(error);
    }

    public DinoSnapshotCollection(
            long start,
            long totalItems,
            int limit,
            List<DinoSnapshot> snapshots) {
        super(start, totalItems, limit, snapshots);
    }
}
