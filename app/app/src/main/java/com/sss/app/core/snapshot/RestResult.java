package com.sss.app.core.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;
import com.sss.app.core.entity.snapshot.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class RestResult extends AbstractErrorSnapshot {
    private List<Integer> snapshotIds = new ArrayList<>();

    public RestResult(List<Integer> snapshotIds) {
        super();
        this.snapshotIds = snapshotIds;
    }

    public RestResult() {
    }

    public RestResult(ErrorResponse error) {
        super(error);
    }

    public RestResult(Integer id) {
        snapshotIds.add(id);
    }

    public void addSnapshotId(Integer id) {
        snapshotIds.add(id);
    }

    public void addSnapshotIds(List<Integer> ids) {
        snapshotIds.addAll(ids);
    }

    @JsonIgnore
    public Integer getSnapshotId() {
        if (snapshotIds.size() > 0)
            return snapshotIds.get(0);
        else
            return null;
    }

    public List<Integer> getSnapshotIds() {
        return snapshotIds;
    }

    public void setSnapshotIds(List<Integer> ids) {
        snapshotIds = ids;
    }
}
