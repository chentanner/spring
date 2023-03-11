package com.sss.app.dinos.model;

import com.sss.app.core.entity.model.AbstractIdEntity;
import com.sss.app.dinos.snapshot.DinoDetail;

import javax.persistence.*;

@Entity
@Table(name = "DINO")
@NamedQueries({
        @NamedQuery(
                name = Dino.FETCH_ALL_DINOS,
                query = "select dino from Dino dino"
        )
})
public class Dino extends AbstractIdEntity {
    public static final String FETCH_ALL_DINOS = "fetch_all_dinos";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "DINO_FAMILY_SK")
    private DinoFamily dinoFamily;

    @Embedded
    private DinoDetail detail = new DinoDetail();

    public Dino() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DinoFamily getDinoFamily() {
        return dinoFamily;
    }

    public void setDinoFamily(DinoFamily dinoFamily) {
        this.dinoFamily = dinoFamily;
    }

    public DinoDetail getDetail() {
        return detail;
    }

    public void setDetail(DinoDetail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Dino{" +
                "id=" + id +
                ", dinoFamily=" + dinoFamily +
                ", detail=" + detail +
                '}';
    }

    @Override
    @Transient
    public String getEntityName() {
        return "Dino";
    }
}
