package com.sss.app.dinos.snapshot;

import java.io.Serializable;

public class DinoSnapshot implements Serializable {
    private Long id;
    private DinoDetail detail = new DinoDetail();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DinoDetail getDetail() {
        return detail;
    }

    public void setDetail(DinoDetail detail) {
        this.detail = detail;
    }
}
