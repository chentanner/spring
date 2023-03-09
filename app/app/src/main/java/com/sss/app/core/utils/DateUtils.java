package com.sss.app.core.utils;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final Logger logger = LogManager.getLogger();

    public static String toString(LocalDate date) {
        String dateString = null;

        if (date != null) {
            dateString = date.format(DateTimeFormatter.ISO_DATE);
        }
        return dateString;
    }

    public static String toStringTime(LocalTime date) {
        String dateString = null;

        if (date != null) {
            dateString = date.format(DateTimeFormatter.ISO_TIME);
        }

        return dateString;
    }

    public static String toStringDateTime(LocalDateTime date) {
        String dateString = null;

        if (date != null) {
            dateString = date.format(DateTimeFormatter.ISO_DATE_TIME);
        }

        return dateString;
    }

    public static LocalDate parseDateFromMillis(long millis) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static LocalDateTime parseDateTimeFromMillis(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static LocalDate parseFormulaDate(String dateIn) {
        if (dateIn == null || dateIn.trim().equals("")) {
            return null;
        }

        String dateString = dateIn.trim();

        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } catch (RuntimeException p) {
            logger.error("Parse failed " + p.getMessage());
            throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_DATE_STRING,
                                                  "Expected a string format of '" + DateTimeFormatter.ISO_DATE +
                                                          "' but found '" + dateIn + "'.");
        }
    }

    public static LocalDate parseDateIgnoreTime(String dateIn) {
        try {
            return parseFormulaDate(dateIn);
        } catch (ApplicationRuntimeException ex) {
            LocalDateTime date = parseFormulaDateTimeMillis(dateIn);
            return date.toLocalDate();
        }
    }

    public static LocalDateTime parseFormulaDateTimeMillis(String dateTimeMillisIn) {

        if (dateTimeMillisIn == null || dateTimeMillisIn.trim().equals("")) {
            return null;
        }

        String dateTimeMillisString = dateTimeMillisIn.trim();

        return LocalDateTime.parse(dateTimeMillisString, DateTimeFormatter.ISO_DATE_TIME);
    }
}
