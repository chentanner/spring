package com.sss.repositoryimpl;

import com.sss.entity.repository.EntityRepository;
import com.sss.model.Dino;
import com.sss.repository.DinoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sss.model.Dino.FETCH_ALL_DINOS;

@Component
public class DinoRepositoryBean extends EntityRepository implements DinoRepository {

    public List<Dino> fetchAll() {
        return getBaseDAO().executeQuery(FETCH_ALL_DINOS);
    }
}
