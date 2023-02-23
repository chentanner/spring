package com.sss.app.dinos.repositoryimpl;

import com.sss.app.core.entity.repository.EntityRepository;
import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.repository.DinoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DinoRepositoryBean extends EntityRepository<Dino> implements DinoRepository {

    public DinoRepositoryBean() {
        super(Dino.class);
    }

    @Override
    public Dino load(int id) {
        return super.load(id);
    }

    public List<Dino> fetchAll() {
        return super.fetchAll(Dino.FETCH_ALL_DINOS);
    }
}

