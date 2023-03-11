package com.sss.app.test;

import com.sss.app.core.entity.model.AuditAbstractEntity;
import com.sss.app.core.entity.model.TemporalAbstractEntity;
import com.sss.app.core.entity.snapshot.AbstractIdSnapshot;
import com.sss.app.core.enums.TransactionErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTestCase {

    private static final Logger logger = LogManager.getLogger();

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    protected void assertNullOrEmpty(String string) {
        if (string != null &&
                !string.isEmpty()) {
            fail("Expected null or empty String. but was '" + string + "'");
        }
    }

    protected void assertDateEquals(Date expected, Date provided) {
        Calendar expectedCal = Calendar.getInstance();
        expectedCal.setTime(expected);
        Calendar providedCal = Calendar.getInstance();
        providedCal.setTime(provided);
        assertEquals(expectedCal.get(Calendar.YEAR), providedCal.get(Calendar.YEAR), "Year");
        assertEquals(expectedCal.get(Calendar.MONTH), providedCal.get(Calendar.MONTH), "Month");
        assertEquals(expectedCal.get(Calendar.DAY_OF_MONTH), providedCal.get(Calendar.DAY_OF_MONTH), "Day");
    }

    protected void assertDateTimeEquals(Date expected, Date provided) {
        Calendar expectedCal = Calendar.getInstance();
        expectedCal.setTime(expected);
        Calendar providedCal = Calendar.getInstance();
        providedCal.setTime(provided);
        assertEquals(expectedCal.get(Calendar.YEAR), providedCal.get(Calendar.YEAR), "Year");
        assertEquals(expectedCal.get(Calendar.MONTH), providedCal.get(Calendar.MONTH), "Month");
        assertEquals(expectedCal.get(Calendar.DAY_OF_MONTH), providedCal.get(Calendar.DAY_OF_MONTH), "Day");
        assertEquals(expectedCal.get(Calendar.HOUR_OF_DAY), providedCal.get(Calendar.HOUR_OF_DAY), "Hour");
        assertEquals(expectedCal.get(Calendar.MINUTE), providedCal.get(Calendar.MINUTE), "Minute");
        assertEquals(expectedCal.get(Calendar.SECOND), providedCal.get(Calendar.SECOND), "Second");
    }

    protected void assertAttributesEqual(TemporalAbstractEntity entity, AbstractIdSnapshot snapshot) {
        assertEquals(entity.getBusinessKey(), snapshot.getBusinessKey());
        assertEquals(entity.getEntityState(), snapshot.getEntityState());
    }

    protected void assertHistory(TemporalAbstractEntity prime, AuditAbstractEntity history) {
        assertNotNull(history.getHistoryDateTimeStamp().getValidFromDateTime());
        assertNotNull(history.getHistoryDateTimeStamp().getValidToDateTime());
    }

    protected void assertCodeEquals(TransactionErrorCode code, String codeStr) {
        assertEquals(code.getCode(), codeStr);
    }

    protected void assertCodeEquals(TransactionErrorCode code1, TransactionErrorCode code2) {
        assertEquals(code1.getCode(), code2.getCode());
    }

    protected void assertCodeEquals(String codeStr, TransactionErrorCode code) {
        assertEquals(code.getCode(), codeStr);
    }
}
