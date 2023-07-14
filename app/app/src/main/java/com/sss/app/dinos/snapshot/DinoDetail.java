package com.sss.app.dinos.snapshot;

import com.sss.app.core.exception.ApplicationValidationException;
import com.sss.app.core.snapshot.BaseDetail;
import com.sss.app.core.snapshot.BaseDetailBuilder;

import javax.persistence.Column;
import java.util.Objects;
import java.util.function.Supplier;


public class DinoDetail extends BaseDetail<DinoDetail> {

    @Column(name = "NAME")
    private String name;

    @Column(name = "FANGS")
    private boolean fangs;

    @Column(name = "NUMBER_OF_ARMS")
    private int numberOfArms;

    @Column(name = "WEIGHT_TONS")
    private double weightTons;

    public DinoDetail() {
    }

    public DinoDetail(String name, boolean fangs, int numberOfArms, double weightTons) {
        this.name = name;
        this.fangs = fangs;
        this.numberOfArms = numberOfArms;
        this.weightTons = weightTons;
    }

    @Override
    public void shallowCopyFrom(DinoDetail copyFrom) {
        this.setName(copyFrom.getName());
        this.setFangs(copyFrom.hasFangs());
        this.setNumberOfArms(copyFrom.getNumberOfArms());
        this.setWeightTons(copyFrom.getWeightTons());
    }

    @Override
    public void validate() throws ApplicationValidationException {

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

    public static class Builder extends BaseDetailBuilder<DinoDetail> {

        private String name;
        private boolean fangs;
        private int numberOfArms;
        private double weightTons;

        private Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFangs(boolean fangs) {
            this.fangs = fangs;
            return this;
        }

        public Builder withNumberOfArms(int numberOfArms) {
            this.numberOfArms = numberOfArms;
            return this;
        }

        public Builder withWeightTons(double weightTons) {
            this.weightTons = weightTons;
            return this;
        }

        @Override
        protected Supplier<DinoDetail> constructorSupplier() {
            return DinoDetail::new;
        }
    }
}
