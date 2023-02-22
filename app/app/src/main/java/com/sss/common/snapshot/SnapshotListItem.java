package com.sss.common.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sss.entity.snapshot.SimpleListItem;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SnapshotListItem implements SimpleListItem {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private boolean selectable = true;

    private SnapshotListDetail detail = new SnapshotListDetail();

    public SnapshotListItem() {
    }

    public SnapshotListItem(Integer id) {
        this.id = id;
    }

    public SnapshotListItem(
            Integer id,
            String name,
            String description,
            Boolean isExpired) {
        this.id = id;
        detail.setName(name);
        detail.setDescription(description);
        detail.setIsExpired(isExpired);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean getSelectable() {
        return selectable;
    }

    @JsonIgnore
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    @JsonIgnore
    public String getCode() {
        return detail.getName();
    }

    @Override
    @JsonIgnore
    public String getDescription() {
        return detail.getDescription();
    }

    @Override
    @JsonIgnore
    public Boolean getExpired() {
        return detail.getIsExpired();
    }

    public SnapshotListDetail getDetail() {
        return detail;
    }

    public void setDetail(SnapshotListDetail detail) {
        this.detail = detail;
    }

    public String toString() {
        if (!detail.getIsExpired())
            return id + " : " + detail.getName() + " : " + detail.getDescription();
        return id + " : " + detail.getName() + " : " + detail.getDescription() + " IsExpired";
    }

}
