package com.sss.app.test;

import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.service.DinoService;
import com.sss.app.dinos.snapshot.DinoSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTest extends TransactionalSpringTestCase {

    @Autowired
    DinoService dinoService;

    @Override
    public void setUp() {

    }

    @DisplayName("Test Spring @Autowired Integration")
    @Test
    void testGet() {
        Dino dino = new Dino();
        dino.getDetail().setName("NewDino");
        dino.save();
        flush();
        List<DinoSnapshot> dinoSnapshots = dinoService.fetch();
        assertNotNull(dinoSnapshots);
    }
}
