package com.sss.app.core.entity.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Calendar;
import java.util.Date;

@Embeddable
public class HistoryDateTimeStamp {
    protected Date validFromDateTime = new Date();
    protected Date validToDateTime = new Date(0);
    protected String lastUserid = "System";

    public HistoryDateTimeStamp() {
    }

    /**
     * Copy Constructor
     *
     * @param historyDateTimeStamp
     */
    public HistoryDateTimeStamp(HistoryDateTimeStamp copy) {
        this.validFromDateTime = copy.validFromDateTime;
        this.validToDateTime = copy.validToDateTime;
    }

    @Column(name = "VALID_FROM_DTTM")
    public Date getValidFromDateTime() {
        return validFromDateTime;
    }

    public void setValidFromDateTime(Date validFromDateTime) {
        this.validFromDateTime = validFromDateTime;
    }

    @Column(name = "VALID_TO_DTTM")
    public Date getValidToDateTime() {
        return validToDateTime;
    }

    public void setValidToDateTime(Date validToDateTime) {
        this.validToDateTime = validToDateTime;
    }

    /**
     * Initialize this history timestamp
     */
    public void initialize(Date validFromDateTime) {
        this.validFromDateTime = validFromDateTime;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(4546, 0, 31, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.validToDateTime = calendar.getTime();
    }

    @Column(name = "LAST_USER")
    public String getLastUserid() {
        return lastUserid;
    }

    public void setLastUserid(String lastUserid) {
        this.lastUserid = lastUserid;
    }

    public String toString() {
        return " From " + validFromDateTime + " to " + validToDateTime;
    }

    /**
     * Determine if a given date is within the valid date range.
     *
     * @param histDate
     * @return
     */
    public boolean isValidFor(Date histDate) {

        if ((histDate.getTime() >= validFromDateTime.getTime()) && (histDate.getTime() < validToDateTime.getTime()))
            return true;
        else
            return false;
    }


    public void version(Date validFromDateTime) {
        this.validFromDateTime = validFromDateTime;
    }

    public void stamp(Date validFromDateTime, Date validToDateTime) {
        this.validFromDateTime = validFromDateTime;
        this.validToDateTime = validToDateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((validFromDateTime == null) ? 0 : validFromDateTime
                .hashCode());
        result = prime * result
                + ((validToDateTime == null) ? 0 : validToDateTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistoryDateTimeStamp other = (HistoryDateTimeStamp) obj;
        if (validFromDateTime == null) {
            if (other.validFromDateTime != null)
                return false;
        } else if (!validFromDateTime.equals(other.validFromDateTime))
            return false;
        if (validToDateTime == null) {
            if (other.validToDateTime != null)
                return false;
        } else if (!validToDateTime.equals(other.validToDateTime))
            return false;
        return true;
    }

}
