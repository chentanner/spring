package com.sss.app.dinos.model;

import com.sss.app.core.entity.model.AbstractIdEntity;
import com.sss.app.dinos.snapshot.DinoFamilyDetail;

import javax.persistence.*;

@Entity
@Table(name = "DINO_FAMILY")
@NamedQueries({
        @NamedQuery(
                name = DinoFamily.FETCH_ALL_DINO_FAMILY,
                query = "select dino from DinoFamily dino"
        )
})
public class DinoFamily extends AbstractIdEntity {
    public static final String FETCH_ALL_DINO_FAMILY = "fetch_all_dino_family";
    @Id
    @Column(name = "DINO_FAMILY_SK")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Embedded
    private DinoFamilyDetail detail = new DinoFamilyDetail();

    public DinoFamily() {
    }

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

    @Override
    public String toString() {
        return "DinoFamily{" +
                "id=" + id +
                ", detail=" + detail +
                '}';
    }

    @Override
    @Transient
    public String getEntityName() {
        return "DinoFamily";
    }
}
