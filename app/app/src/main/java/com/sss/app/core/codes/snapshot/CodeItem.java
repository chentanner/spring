package com.sss.app.core.codes.snapshot;

import java.io.Serializable;

public class CodeItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private String label;

    public CodeItem() {
    }

    public CodeItem(
            String code,
            String description,
            String label
    ) {
        super();
        this.code = code;
        this.description = description;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "CodeItem{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
