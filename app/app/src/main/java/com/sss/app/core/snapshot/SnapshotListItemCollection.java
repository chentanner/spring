package com.sss.app.core.snapshot;

import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;

import java.util.List;

public class SnapshotListItemCollection extends BaseSnapshotCollection<SnapshotListItem> {
    private String itemType;

    public SnapshotListItemCollection() {
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    public SnapshotListItemCollection(AbstractErrorSnapshot error) {
        super(error);
    }

    public SnapshotListItemCollection(
            long start,
            long totalItems,
            int limit,
            List<SnapshotListItem> items,
            String itemType) {
        super(start,
              totalItems,
              limit,
              items);
        this.itemType = itemType;
    }

}