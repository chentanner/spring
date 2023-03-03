package com.sss.app.dinos.snapshot;

import com.sss.app.core.entity.snapshot.ErrorResponse;
import com.sss.app.core.snapshot.BaseSnapshotCollection;

import java.util.List;

public class DinoSnapshotCollection extends BaseSnapshotCollection<DinoSnapshot> {
    public static String itemType = "application/vnd.sss.dinos";

    public DinoSnapshotCollection() {
    }

    public DinoSnapshotCollection(ErrorResponse error) {
        super(error);
    }

    public DinoSnapshotCollection(
            long start,
            long totalItems,
            int limit,
            List<DinoSnapshot> snapshots) {
        super(start, totalItems, limit, snapshots);
    }

    @Override
    public String getItemType() {
        return itemType;
    }
}
