package com.sss.app.core.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WarningSnapshot {
    private String warningCode = null;
    private String warningMessage = null;

    public WarningSnapshot() {
    }

    public WarningSnapshot(String warningCode, String warningMessage) {
        this.warningCode = warningCode;
        this.warningMessage = warningMessage;
    }

    @JsonIgnore
    public boolean hasWarning() {
        return warningCode != null;
    }

    public String getWarningCode() {
        return warningCode;
    }

    public void setWarningCode(String warningCode) {
        this.warningCode = warningCode;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
