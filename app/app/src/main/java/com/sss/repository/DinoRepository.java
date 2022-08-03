package com.sss.repository;

import com.sss.entity.repository.IEntityRepository;
import com.sss.model.Dino;

import java.util.List;

public interface DinoRepository extends IEntityRepository {
    public List<Dino> fetchAll();
}
