package com.sss.app.core.query.expressions;

import com.sss.app.core.entity.model.IEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public enum QueryPropertyType {
    STRING(String.class),
    TEXT(String.class),
    ENTITY(IEntity.class),
    BOOLEAN(Boolean.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BIG_DECIMAL(BigDecimal.class),
    /**
     * QueryPropertyType's equal to DATE are corrected when they are serialized
     * to/from the UI client and the server.  That means that the adjustment made by
     * BlazeDS as dates move from client to server is corrected.
     * For example June 1, 2018 on a client that is in Mountain Standard Time will arrive
     * as June 1, 2018 on a server that is in Eastern Standard Time and not June 2, 2018 2am.
     */
    DATE(LocalDate.class),
    /**
     * QueryPropertyType's equal to DATE_TIMEZONE_SENSITIVE are not corrected when they are serialized
     * to/from the UI client and the server.  That means that the adjustment made by
     * BlazeDS as dates move from client to server is not corrected.
     * For example June 1, 2018 on a client that is in Mountain Standard Time will arrive
     * as June 2, 2018 2am on a server that is in Eastern Standard Time.
     */
    DATE_TIMEZONE_SENSITIVE(ZonedDateTime.class),
    /**
     * QueryPropertyType's equal to DATETIME are corrected when they are serialized
     * to/from the UI client and the server.  That means that the adjustment made by
     * BlazeDS as dates move from client to server is corrected.
     * For example June 1, 2018 9:30am on a client that is in Mountain Standard Time will arrive
     * as June 1, 2018 9:30am on a server that is in Eastern Standard Time and not June 1, 2018 11:30am.
     */
    DATETIME(LocalDateTime.class),
    /**
     * QueryPropertyType's equal to DATETIME_TIMEZONE_SENSITIVE are not corrected when they are serialized
     * to/from the UI client and the server.  That means that the adjustment made by
     * BlazeDS as dates move from client to server is not corrected.
     * For example June 1, 2018 9:30am on a client that is in Mountain Standard Time will arrive
     * as June 1, 2018 11:30am on a server that is in Eastern Standard Time.
     */

    DATETIME_TIMEZONE_SENSITIVE(ZonedDateTime.class),
    NULL(null);

    private final Class claz;

    private QueryPropertyType(Class clasz) {
        this.claz = clasz;
    }

    public boolean isDate() {
        return claz.equals(Date.class);
    }

    public boolean isDateTime() {
        return (this == QueryPropertyType.DATETIME || this == QueryPropertyType.DATETIME_TIMEZONE_SENSITIVE);
    }

    public Object convertValue(Object o) {
        return o instanceof Iterable ? convertMany((Iterable) o) : convertOne(o);
    }

    private List convertMany(Iterable items) {
        ArrayList<Object> result = new ArrayList<>();
        for (Object item : items) {
            result.add(convertOne(item));
        }
        return result;
    }

    public Class getTypeClass() {
        return claz;
    }

    private Object convertOne(Object o) {
        if (!(o instanceof String)) {
            return o;
        }
        String raw = (String) o;
        if (this == BIG_DECIMAL) {
            return new BigDecimal(raw);
        }
        return o;
    }
}
