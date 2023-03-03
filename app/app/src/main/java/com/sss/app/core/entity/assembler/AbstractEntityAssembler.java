package com.sss.app.core.entity.assembler;

import com.sss.app.core.entity.model.VersionedEntity;
import com.sss.app.core.entity.snapshot.AbstractEntitySnapshot;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AbstractEntityAssembler<T extends AbstractEntitySnapshot> extends AbstractIdAssembler<T> {
    private static final Logger logger = LogManager.getLogger(AbstractEntityAssembler.class);

    public AbstractEntityAssembler() {
    }

    protected void setBaseEntityAttributes(VersionedEntity entity, T snapshot) {
        super.setBaseEntityAttributes(entity, snapshot);
        if (entity == null) {
            throw new ApplicationRuntimeException(TransactionErrorCode.NULL_ENTITY_ENCOUNTERED);
        }

        snapshot.setEntityState(entity.getEntityState());
        snapshot.setExpired(entity.getIsExpired());

        if (entity.getVersion() != null) {
            snapshot.setVersion(entity.getVersion());
        }
    }
}