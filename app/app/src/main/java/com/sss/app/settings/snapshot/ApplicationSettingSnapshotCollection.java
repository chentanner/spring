package com.sss.app.settings.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSettingSnapshotCollection {

    public static String itemType = "application/vnd.sss.applicationsetting";
    private long start;
    private long totalItems;
    private int limit;

    private String errorCode = null;
    private String errorMessage = null;
    private boolean wasSuccessful = true;
    private boolean isPermissionException = false;

    private List<ApplicationSettingSnapshot> controls = new ArrayList<>();

    public ApplicationSettingSnapshotCollection() {
    }

    public ApplicationSettingSnapshotCollection(String errorCode) {
        this.errorCode = errorCode;
        this.isPermissionException = false;
        wasSuccessful = false;
    }

    public ApplicationSettingSnapshotCollection(String errorCode, boolean isPermissionException) {
        this.errorCode = errorCode;
        this.isPermissionException = isPermissionException;
        wasSuccessful = false;
    }

    public ApplicationSettingSnapshotCollection(
            List<ApplicationSettingSnapshot> snapshots,
            long start,
            int limit,
            long totalItems) {

        this.controls = snapshots;
        this.totalItems = totalItems;
        this.start = start;
        this.limit = limit;
    }

    public List<ApplicationSettingSnapshot> getItems() {
        return controls;
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
        return controls.size();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }


}