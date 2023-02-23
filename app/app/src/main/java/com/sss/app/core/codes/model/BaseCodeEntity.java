package com.sss.app.core.codes.model;

import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.repository.BaseRepository;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Transient;

public class BaseCodeEntity implements CodeEntity {

    private CodeEntityPk primaryKey = new CodeEntityPk();

    private Integer displayOrderNo;

    private String label;

    @Transient
    public String getCode() {
        return primaryKey.getCode();
    }

    @EmbeddedId
    public CodeEntityPk getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CodeEntityPk primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Column(name = "CODE_LABEL")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "DISPLAY_ORDER_NO")
    public Integer getDisplayOrderNo() {
        return displayOrderNo;
    }

    public void setDisplayOrderNo(Integer displayOrderNo) {
        this.displayOrderNo = displayOrderNo;
    }

    @Transient
    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }

}
