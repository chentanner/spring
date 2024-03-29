package com.sss.app.dinos.assembler;

import com.sss.app.dinos.model.Dino;
import com.sss.app.dinos.snapshot.DinoSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class DinoAssembler {
    public static List<DinoSnapshot> toSnapshots(List<Dino> fromDino) {
        return fromDino.stream()
                .map(DinoAssembler::toSnapshot)
                .collect(Collectors.toList());
    }

    public static DinoSnapshot toSnapshot(Dino fromDino) {
        DinoSnapshot toDinoSnapshot = new DinoSnapshot();
        toDinoSnapshot.setId(fromDino.getId());
        toDinoSnapshot.getDetail().shallowCopy(fromDino.getDetail());
        return toDinoSnapshot;
    }

    public static Dino toEntity() {
        return null;
    }
}
