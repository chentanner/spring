package com.sss.app.dinos.snapshot;

import java.io.Serializable;

public class DinoSnapshot implements Serializable {
    private Integer id;
    private DinoDetail detail = new DinoDetail();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DinoDetail getDetail() {
        return detail;
    }

    public void setDetail(DinoDetail detail) {
        this.detail = detail;
    }
}
