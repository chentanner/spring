package com.sss.app.dinos.snapshot;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.core.entity.snapshot.AbstractEntitySnapshot;

import java.io.Serializable;

public class DinoSnapshot extends AbstractEntitySnapshot implements Serializable {
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

    public static class Builder {
        private Integer id;
        private EntityState entityState = EntityState.NEW;
        DinoDetail.Builder detailBuilder;

        private Builder() {

        }

        public DinoSnapshot.Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DinoSnapshot.Builder withEntityState(EntityState entityState) {
            this.entityState = entityState;
            return this;
        }

        public DinoSnapshot.Builder withBuilderBuilder(DinoDetail.Builder builderBuilder) {
            this.detailBuilder = builderBuilder;
            return this;
        }

        public DinoSnapshot build() {
            DinoSnapshot dinoSnapshot = new DinoSnapshot();
            dinoSnapshot.setId(id);
            dinoSnapshot.setEntityState(entityState);
            dinoSnapshot.setDetail(detailBuilder.build());
            return dinoSnapshot;
        }
    }
}
