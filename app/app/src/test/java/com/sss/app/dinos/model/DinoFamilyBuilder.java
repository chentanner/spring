package com.sss.app.dinos.model;

import com.sss.app.core.entity.enums.EntityState;
import com.sss.app.dinos.snapshot.DinoFamilyDetail;
import com.sss.app.dinos.snapshot.DinoFamilySnapshot;
import com.sss.app.test.BaseEntityBuilder;

import java.util.List;

public class DinoFamilyBuilder extends BaseEntityBuilder<DinoFamily, DinoFamilySnapshot> {
    private Integer id;
    private EntityState entityState = EntityState.NEW;

    DinoFamilyDetail.Builder detailBuilder;

    public static DinoFamilyBuilder of() {
        return new DinoFamilyBuilder();
    }

    private DinoFamilyBuilder() {

    }

    public DinoFamilyBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public DinoFamilyBuilder withEntityState(EntityState entityState) {
        this.entityState = entityState;
        return this;
    }

    public DinoFamilyBuilder withBuilderBuilder(DinoFamilyDetail.Builder builderBuilder) {
        this.detailBuilder = builderBuilder;
        return this;
    }

    public DinoFamily build() {
        DinoFamily dinoFamily = new DinoFamily();
        dinoFamily.setId(id);
        dinoFamily.setDetail(detailBuilder.build());
        return dinoFamily;
    }

    @Override
    public DinoFamily buildSavedEntity() {
        DinoFamily dinoFamily = build();
        dinoFamily.save();
        return dinoFamily;
    }

    @Override
    public List<DinoFamily> buildSavedEntities(int numEntities) {
        return null;
    }

    @Override
    public DinoFamilySnapshot buildSnapshot() {
        DinoFamilySnapshot dinoFamilySnapshot = new DinoFamilySnapshot();
        dinoFamilySnapshot.setId(id);
        dinoFamilySnapshot.setEntityState(entityState);
        dinoFamilySnapshot.setDetail(detailBuilder.build());
        return dinoFamilySnapshot;
    }
}
