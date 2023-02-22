package com.sss.app.dinos.repositoryimpl;

import com.sss.app.core.entity.repository.EntityRepository;
import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.repository.DinoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DinoRepositoryBean extends EntityRepository implements DinoRepository {

    public List<Dino> fetchAll() {
        return getBaseDAO().executeQuery(Dino.FETCH_ALL_DINOS);
    }
}
