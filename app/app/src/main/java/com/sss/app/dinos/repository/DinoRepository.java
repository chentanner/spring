package com.sss.app.dinos.repository;

import com.sss.app.core.entity.repository.IEntityRepository;
import com.sss.app.dinos.model.Dino;

import java.util.List;

public interface DinoRepository extends IEntityRepository {
    public List<Dino> fetchAll();
}
