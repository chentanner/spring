package com.sss.app.dinos.snapshot;

import com.sss.app.core.query.expressions.QueryProperty;
import com.sss.app.core.query.expressions.QueryPropertyType;
import com.sss.app.core.query.propertymapper.BaseQueryProperties;
import com.sss.app.dinos.model.Dino;

public class DinoQueryProperties extends BaseQueryProperties {
    public static final String entityName = Dino.class.getName();

    public static final QueryProperty DINO_NAME_QP =
            new QueryProperty(
                    entityName,
                    "name",
                    "detail.name",
                    QueryPropertyType.STRING
            );

    public static final QueryProperty DINO_FANGS_QP =
            new QueryProperty(
                    entityName,
                    "fangs",
                    "detail.fangs",
                    QueryPropertyType.BOOLEAN
            );
    
    public DinoQueryProperties() {
        super(entityName);
        add(DINO_NAME_QP);
        add(DINO_FANGS_QP);
    }
}
