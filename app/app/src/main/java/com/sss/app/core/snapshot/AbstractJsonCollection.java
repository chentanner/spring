package com.sss.app.core.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJsonCollection<T> {

    protected long start;
    protected long totalItems;
    protected int limit;

    protected List<T> items = new ArrayList<>();

    public AbstractJsonCollection() {
    }

    public AbstractJsonCollection(
            long start,
            long totalItems,
            int limit,
            List<T> items) {
        this.items = items;
        this.totalItems = totalItems;
        this.start = start;
        this.limit = limit;
    }

    public abstract String getItemType();

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> snapshots) {
        this.items = snapshots;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    @JsonIgnore
    public long getCount() {
        return items.size();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

