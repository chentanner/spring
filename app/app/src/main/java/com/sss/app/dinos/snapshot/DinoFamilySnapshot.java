package com.sss.app.dinos.snapshot;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.core.entity.snapshot.AbstractEntitySnapshot;

import java.io.Serializable;

public class DinoFamilySnapshot extends AbstractEntitySnapshot implements Serializable {
    private Integer id;
    private DinoFamilyDetail detail = new DinoFamilyDetail();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DinoFamilyDetail getDetail() {
        return detail;
    }

    public void setDetail(DinoFamilyDetail detail) {
        this.detail = detail;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private EntityState entityState = EntityState.NEW;
        DinoFamilyDetail.Builder builderBuilder;

        private Builder() {

        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withEntityState(EntityState entityState) {
            this.entityState = entityState;
            return this;
        }

        public Builder withBuilderBuilder(DinoFamilyDetail.Builder builderBuilder) {
            this.builderBuilder = builderBuilder;
            return this;
        }

        public DinoFamilySnapshot build() {
            DinoFamilySnapshot dinoFamilySnapshot = new DinoFamilySnapshot();
            dinoFamilySnapshot.setId(id);
            dinoFamilySnapshot.setEntityState(entityState);
            dinoFamilySnapshot.setDetail(builderBuilder.build());
            return dinoFamilySnapshot;
        }
    }
}
