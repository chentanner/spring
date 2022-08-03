package com.sss.model;

import com.sss.snapshot.DinoDetail;

import javax.persistence.*;

@Entity
@Table(name = "DINO")
@NamedQueries({
        @NamedQuery(
                name = Dino.FETCH_ALL_DINOS,
                query = "select dino from Dino dino"
        )
})
public class Dino {
    public static final String FETCH_ALL_DINOS = "fetch_all_dinos";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DinoDetail detail;

    public Dino() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", detail=" + detail +
                '}';
    }
}
