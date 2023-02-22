package com.sss.app.dinos.serviceimpl;

import com.sss.app.dinos.assembler.DinoAssembler;
import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.repository.DinoRepository;
import com.sss.app.dinos.service.DinoService;
import com.sss.app.dinos.snapshot.DinoSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DinoServiceBean implements DinoService {
    @Autowired
    DinoRepository dinoRepository;

    public List<DinoSnapshot> fetch() {
        List<Dino> dinos = dinoRepository.fetchAll();
        return DinoAssembler.toSnapshots(dinos);
    }
}
