package com.sss.snapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSnapshotCollection<T> {
    private long start;
    private long totalItems;
    private int limit;
    private ErrorSnapshot error;
    private List<T> snapshots = new ArrayList<>();

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
        this.start = start;
        this.totalItems = totalItems;
        this.limit = limit;
        this.snapshots = snapshots;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ErrorSnapshot getError() {
        return error;
    }

    public void setError(ErrorSnapshot error) {
        this.error = error;
    }

    public List<T> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<T> snapshots) {
        this.snapshots = snapshots;
    }
}
