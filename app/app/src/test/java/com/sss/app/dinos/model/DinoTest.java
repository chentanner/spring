package com.sss.app.dinos.model;

import com.sss.app.core.query.expressions.QueryCriteria;
import com.sss.app.dinos.repository.DinoRepository;
import com.sss.app.test.TransactionalSpringTestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DinoTest extends TransactionalSpringTestCase {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void setUp() {

    }

    @Test
    public void dinoSave() {

        Dino dino = new Dino();
        String dinoName = "NewDino";
        dino.getDetail().setName(dinoName);
        dino.save();

        DinoRepository dinoRepository = getBean(DinoRepository.class);
        Dino foundDino = dinoRepository.findSingleResult(new QueryCriteria<>(Dino.class));
        assertNotNull(foundDino);
    }

    @Test
    public void dinoFetchAll() {

        Dino dino = new Dino();
        String dinoName = "NewDino";
        dino.getDetail().setName(dinoName);
        dino.save();
        flush();
        DinoRepository dinoRepository = getBean(DinoRepository.class);
        List<Dino> foundDinos = dinoRepository.fetchAllDinos();
        assertFalse(foundDinos.isEmpty());
    }
}