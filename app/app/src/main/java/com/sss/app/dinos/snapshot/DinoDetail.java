package com.sss.app.dinos.snapshot;

import javax.persistence.Column;
import java.util.Objects;


public class DinoDetail {

    @Column(name = "NAME")
    private String name;

    @Column(name = "FANGS")
    private boolean fangs;

    @Column(name = "NUMBER_OF_ARMS")
    private int numberOfArms;

    @Column(name = "WEIGHT_TONS")
    private double weightTons;

    public void shallowCopy(DinoDetail copyFrom) {
        this.setName(copyFrom.getName());
        this.setFangs(copyFrom.hasFangs());
        this.setNumberOfArms(copyFrom.getNumberOfArms());
        this.setWeightTons(copyFrom.getWeightTons());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasFangs() {
        return fangs;
    }

    public void setFangs(boolean fangs) {
        this.fangs = fangs;
    }

    public int getNumberOfArms() {
        return numberOfArms;
    }

    public void setNumberOfArms(int numberOfArms) {
        this.numberOfArms = numberOfArms;
    }

    public double getWeightTons() {
        return weightTons;
    }

    public void setWeightTons(double weightTons) {
        this.weightTons = weightTons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinoDetail that = (DinoDetail) o;
        return fangs == that.fangs && numberOfArms == that.numberOfArms && Double.compare(that.weightTons,
                                                                                          weightTons) == 0 && Objects.equals(
                name,
                that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fangs, numberOfArms, weightTons);
    }

    @Override
    public String toString() {
        return "DinoDetail{" +
                "name='" + name + '\'' +
                ", fangs=" + fangs +
                ", numberOfArms=" + numberOfArms +
                ", weightTons=" + weightTons +
                '}';
    }
}
