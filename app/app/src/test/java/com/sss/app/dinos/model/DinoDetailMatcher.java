package com.sss.app.dinos.model;

import com.sss.app.dinos.snapshot.DinoDetail;
import com.sss.app.test.matcher.BaseMatcher;
import com.sss.app.test.matcher.EqualsMatcher;

public class DinoDetailMatcher extends BaseMatcher<DinoDetail> {
    private EqualsMatcher<DinoDetail, ?> nameMatcher;
    private EqualsMatcher<DinoDetail, ?> numberOfArmsMatcher;
    private EqualsMatcher<DinoDetail, ?> weightMatcher;
    private EqualsMatcher<DinoDetail, ?> hasFangsMatcher;

    public static DinoDetailMatcher of() {
        return new DinoDetailMatcher();
    }

    public static DinoDetailMatcher of(DinoDetail expectedDetail) {
        return new DinoDetailMatcher()
                .withName(expectedDetail.getName())
                .withWeightTons(expectedDetail.getWeightTons())
                .withNumberOfArms(expectedDetail.getNumberOfArms())
                .withFangs(expectedDetail.hasFangs());
    }

    public DinoDetailMatcher withName(String name) {
        nameMatcher = new EqualsMatcher<>(DinoDetail::getName, name);
        return this;
    }

    public DinoDetailMatcher withNumberOfArms(int numberOfArms) {
        numberOfArmsMatcher = new EqualsMatcher<>(DinoDetail::getNumberOfArms, numberOfArms);
        return this;
    }

    public DinoDetailMatcher withWeightTons(double weight) {
        weightMatcher = new EqualsMatcher<>(DinoDetail::getWeightTons, weight);
        return this;
    }

    public DinoDetailMatcher withFangs(boolean hasFangs) {
        hasFangsMatcher = new EqualsMatcher<>(DinoDetail::hasFangs, hasFangs);
        return this;
    }

    @Override
    public void matches(DinoDetail aliasDetail) {
        BaseMatcher.safeMatch(nameMatcher, aliasDetail);
        BaseMatcher.safeMatch(numberOfArmsMatcher, aliasDetail);
        BaseMatcher.safeMatch(weightMatcher, aliasDetail);
        BaseMatcher.safeMatch(hasFangsMatcher, aliasDetail);
    }

}
