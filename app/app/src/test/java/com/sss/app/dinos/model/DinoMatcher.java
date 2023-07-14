package com.sss.app.dinos.model;

import com.sss.app.test.matcher.BaseMatcher;
import com.sss.app.test.matcher.EqualsMatcher;

public class DinoMatcher extends BaseMatcher<Dino> {

    private EqualsMatcher<Dino, ?> idMatcher;
    private EqualsMatcher<Dino, ?> dinoFamilyMatcher;
    private DinoDetailMatcher detailMatcher;

    public static DinoMatcher of() {
        return new DinoMatcher();
    }

    public static DinoMatcher of(Dino expectedEntity) {
        return new DinoMatcher()
                .withId(expectedEntity.getId())
                .withDinoFamily(expectedEntity.getDinoFamily())
                .withDetailMatcher(DinoDetailMatcher.of(expectedEntity.getDetail()));
    }

    public DinoMatcher() {
        detailMatcher = new DinoDetailMatcher();
    }

    public DinoMatcher withId(Integer counterpartyAliasId) {
        idMatcher = new EqualsMatcher<>(Dino::getId,
                                        counterpartyAliasId);
        return this;
    }

    public DinoMatcher withDinoFamily(DinoFamily family) {
        return withDinoFamilyId(family.getId());
    }

    public DinoMatcher withDinoFamilyId(Integer dinoFamilyId) {
        dinoFamilyMatcher = new EqualsMatcher<>((entity) -> entity.getDinoFamily().getId(),
                                                dinoFamilyId);
        return this;
    }

    public DinoMatcher withDetailMatcher(DinoDetailMatcher detail) {
        this.detailMatcher = detail;
        return this;
    }

    @Override
    public void matches(Dino alias) {
        BaseMatcher.safeMatch(idMatcher, alias);
        BaseMatcher.safeMatch(dinoFamilyMatcher, alias);
        detailMatcher.matches(alias.getDetail());
    }
}