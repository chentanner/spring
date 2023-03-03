package com.sss.app.core.entity.assembler;

import com.sss.app.core.entity.model.IEntity;
import com.sss.app.core.entity.snapshot.AbstractIdSnapshot;
import com.sss.app.core.entity.snapshot.BusinessKey;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AbstractIdAssembler<T extends AbstractIdSnapshot> {
    private static final Logger logger = LogManager.getLogger(AbstractIdAssembler.class);

    public AbstractIdAssembler() {
    }

    protected void setBaseEntityAttributes(IEntity entity, T snapshot) {
        if (entity == null) {
            throw new ApplicationRuntimeException(TransactionErrorCode.NULL_ENTITY_ENCOUNTERED);
        }

        if (entity.getId() == null) {
            throw new ApplicationRuntimeException(TransactionErrorCode.BAD_ENTITY_ID,
                                                  "Entities that have not been persisted (i.e. do not have an id) cannot not be assembled.");
        }
        snapshot.setEntityId(entity.getId());
    }

    /**
     * Creates a BusinessKey from the passed key.
     * If key is null, a BusinessKey with IS_NULL set to true is returned.
     *
     * @param key the integer key to be wrapped.
     * @return a BusinessKey.
     */
    protected BusinessKey createBusinessKey(Integer key) {
        if (key == null) {
            BusinessKey bk = new BusinessKey();
            bk.setToNull();
            return bk;
        } else {
            return new BusinessKey(key);
        }
    }
}