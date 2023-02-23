package com.sss.app.core.entity.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;

import java.io.Serializable;

public class BusinessKey implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String SEPARATOR = "/";
    public static final String IS_NULL = "/null/";
    public static final String ID_MARKER = "Id";
    private String value;

    public BusinessKey() {
    }


    public BusinessKey(Integer id) {
        addComponent(ID_MARKER);
        addComponent(id.toString());
    }

    public static BusinessKey createNullKey() {
        BusinessKey key = new BusinessKey();
        key.setToNull();
        return key;
    }

    public void setToNull() {
        value = IS_NULL;
    }

    @JsonIgnore
    public boolean isNotNull() {
        return (value.equals(IS_NULL) == false);
    }

    @JsonIgnore
    public boolean isNull() {
        if (value == null)
            return true;
        return value.equals(IS_NULL) || value.equals("");
    }

    @JsonIgnore
    public boolean isValid() {
        return !(
                value == null ||
                        value.equals("") ||
                        value.equals(IS_NULL)
        );
    }

    public Integer getId() {
        if (isId() == false)
            return null;
        String[] comps = parseKey();
        return Integer.decode(comps[1]);
    }

    @JsonIgnore
    public boolean isId() {
        String[] comps = parseKey();
        if (comps.length != 2)
            return false;
        return (comps[0].equalsIgnoreCase(ID_MARKER));
    }

    public BusinessKey(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Set the entire raw URI including start and end separators.
     *
     * @param value as a URI.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * parse key into separate string tokens
     *
     * @return an array of Strings. or null if the key is null.
     */
    private String[] parseKey() {

        if (value == null) {
            String[] empty = {};
            return empty;
        }

        String[] parts = value.split(SEPARATOR);

        if (!this.isValid()) {
            throw new ApplicationRuntimeException(TransactionErrorCode.BAD_BUSINESS_KEY);
        } else if (parts.length < 2) {
            return null;
        }

        String[] components = new String[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            part = convertContentBarsToSlash(part);
            components[i - 1] = part;
        }
        return components;
    }


    /**
     * @param componentIn
     */
    private void addComponent(String componentIn) {
        String componentParsed = convertContentSlashToBars(componentIn);
        if (value == null) {
            value = SEPARATOR + componentParsed + SEPARATOR;
        } else {
            value = value + componentParsed + SEPARATOR;
        }
    }

    private String convertContentSlashToBars(String strIn) {
        return strIn.replaceAll("/", "||");
    }

    private String convertContentBarsToSlash(String strIn) {
        return strIn.replaceAll("\\|\\|", "/");
    }

    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        BusinessKey other = (BusinessKey) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }


    public static BusinessKey createNullBusinessKey() {
        return new BusinessKey(IS_NULL);
    }
}