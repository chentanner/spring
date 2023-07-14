package com.sss.app.dinos.model;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.dinos.snapshot.DinoDetail;
import com.sss.app.dinos.snapshot.DinoSnapshot;

public class DinoBuilder {

    private Integer id;
    private EntityState entityState = EntityState.NEW;

    DinoFamilyBuilder dinoFamilyBuilder = DinoFamilyBuilder.of();

    DinoDetail.Builder detailBuilder;


    public static DinoBuilder of() {
        return new DinoBuilder();
    }

    private DinoBuilder() {

    }

    public DinoBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public DinoBuilder withEntityState(EntityState entityState) {
        this.entityState = entityState;
        return this;
    }

    public DinoBuilder withBuilderBuilder(DinoDetail.Builder builderBuilder) {
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
