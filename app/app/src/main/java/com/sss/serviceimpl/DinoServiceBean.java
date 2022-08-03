package com.sss.serviceimpl;

import com.sss.assembler.DinoAssembler;
import com.sss.model.Dino;
import com.sss.repository.DinoRepository;
import com.sss.service.DinoService;
import com.sss.snapshot.DinoSnapshot;
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
