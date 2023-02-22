package com.sss.app.dinos.service;

import com.sss.app.dinos.snapshot.DinoSnapshot;

import java.util.List;

public interface DinoService {
    public List<DinoSnapshot> fetch();
}
