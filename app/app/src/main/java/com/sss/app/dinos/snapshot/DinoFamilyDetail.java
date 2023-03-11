package com.sss.app.dinos.snapshot;

import javax.persistence.Column;
import java.util.Objects;


public class DinoFamilyDetail {

    @Column(name = "NAME")
    private String name;


    public void shallowCopy(DinoFamilyDetail copyFrom) {
        this.setName(copyFrom.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinoFamilyDetail that = (DinoFamilyDetail) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "DinoFamilyDetail{" +
                "name='" + name + '\'' +
                '}';
    }
}
