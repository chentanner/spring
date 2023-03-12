package com.sss.app.core.query.expressions;

import com.sss.app.core.enums.TransactionErrorCode;
import com.sss.app.core.exception.ApplicationRuntimeException;
import com.sss.app.core.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class QueryProperty implements Serializable, Comparable<QueryProperty> {
    private static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = 1L;
    protected static final String JDBC_DATE_FORMAT = "yyyy:MM:dd";
    protected static final String JDBC_DATETIME_FORMAT = "yyyy:MM:dd HH:mm:ss";
    protected static final String ENTITY_PREFIX = "entity.";

    private String queryEntityName;

    private String joinQueryEntityName;

    private String label;

    private String name;

    private QueryPropertyType type;

    private Boolean isFormula = false;

    private List<String> valueTypeHintList;

    private Function<String, String> convertNameToCodeFunction;

    private QueryPropertyHintFunction hintFunction;
    private List<Object> parameters;

    private Supplier<List<String>> generateCodeHintsFunction;

    public QueryProperty() {
        valueTypeHintList = generateTypeHints();
    }

    public QueryProperty(QueryProperty copyFrom) {
        this.queryEntityName = copyFrom.queryEntityName;
        this.joinQueryEntityName = copyFrom.joinQueryEntityName;
        this.label = copyFrom.label;
        this.name = copyFrom.name;
        this.type = copyFrom.type;
        this.isFormula = copyFrom.isFormula;
        this.convertNameToCodeFunction = copyFrom.convertNameToCodeFunction;
        this.hintFunction = copyFrom.hintFunction;
        this.parameters = copyFrom.parameters;
        this.generateCodeHintsFunction = copyFrom.generateCodeHintsFunction;

        if (copyFrom.valueTypeHintList != null) {
            this.valueTypeHintList = new ArrayList<>();
            this.valueTypeHintList.addAll(copyFrom.valueTypeHintList);
        }
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            QueryPropertyType type
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = type;
        valueTypeHintList = generateTypeHints();
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            QueryPropertyType type,
            // "parameters" is before the method that accepts the parameters, which seems unnatural.
            // If the order is reversed, there is a collision with one of the other
            // constructors when the second to last parameter (aka parameters) in this constructor is null.
            List<Object> parameters,
            QueryPropertyHintFunction hintFunction
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = type;
        this.hintFunction = hintFunction;
        this.parameters = parameters;
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            QueryPropertyType type,
            Function<String, String> convertNameToCodeFunction,
            Supplier<List<String>> generateCodeHintsFunction
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = type;
        this.convertNameToCodeFunction = convertNameToCodeFunction;
        this.generateCodeHintsFunction = generateCodeHintsFunction;
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            QueryPropertyType type,
            Function<String, String> convertNameToCodeFunction,
            List<String> hints
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = type;
        this.convertNameToCodeFunction = convertNameToCodeFunction;
        if (convertNameToCodeFunction != null) {
            this.valueTypeHintList = addSingleQuotes(hints);
        } else {
            this.valueTypeHintList = hints;
        }
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            QueryPropertyType type,
            Boolean isFormula
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = type;
        this.isFormula = isFormula;
        valueTypeHintList = generateTypeHints();
    }

    public QueryProperty(
            String queryEntityName,
            String label,
            String name,
            String joinQueryEntityName
    ) {
        super();
        this.queryEntityName = queryEntityName;
        this.label = label;
        this.name = name;
        this.type = QueryPropertyType.ENTITY;
        this.joinQueryEntityName = joinQueryEntityName;
    }

    public boolean isPropertyInherited(String parentQueryEntityName) {
        if (queryEntityName.equals("AbstractEntity")) {
            return true;
        }

        return queryEntityName.equals("CodeEntity");
    }

    public boolean isJoinProperty() {
        return joinQueryEntityName != null;
    }

    public boolean supportsFilteredHints() {
        return (type == QueryPropertyType.DATE || type == QueryPropertyType.DATETIME);
    }

    public void shallowCopyFrom(QueryProperty queryProperty) {
        this.queryEntityName = queryProperty.queryEntityName;
        this.label = queryProperty.label;
        this.name = queryProperty.name;
        this.type = queryProperty.type;
        this.valueTypeHintList = queryProperty.valueTypeHintList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public boolean isEnum() {
        return convertNameToCodeFunction != null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QueryPropertyType getType() {
        return type;
    }

    public void setType(QueryPropertyType type) {
        this.type = type;
    }

    public QueryPropertyHintFunction getHintFunction() {
        return hintFunction;
    }

    public void setHintFunction(QueryPropertyHintFunction hintFunction) {
        this.hintFunction = hintFunction;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public Boolean getIsFormula() {
        return isFormula;
    }

    public void setIsFormula(Boolean isFormula) {
        this.isFormula = isFormula;
    }

    public boolean hasTypeHints() {
        return (valueTypeHintList != null);
    }

    public boolean willProvideHints() {
        return (
                generateCodeHintsFunction != null ||
                        valueTypeHintList != null ||
                        hintFunction != null
        );
    }

    public boolean hasEnumHints() {
        return generateCodeHintsFunction != null;
    }

    public List<String> fetchHints() {
        if (generateCodeHintsFunction != null) {
            return addSingleQuotes(generateCodeHintsFunction.get());
        } else if (getHintFunction() != null) {
            return addSingleQuotes(
                    getHintFunction().getHints(
                            getParameters()
                    )
            );
        } else if (valueTypeHintList != null) {
            return valueTypeHintList;
        } else {
            return new ArrayList<>();
        }
    }

    private List<String> addSingleQuotes(List<String> hints) {

        if (hints.isEmpty()) {
            return hints;
        }

        List<String> quotedHints = new ArrayList<>();
        hints.forEach(
                c -> quotedHints.add(
                        c.replaceAll(
                                "'",
                                "''")
                )
        );

        return quotedHints
                .stream()
                .map(c -> "'" + c + "'")
                .collect(Collectors.toList()
                );
    }

    private List<String> generateTypeHints() {
        ArrayList<String> hints = new ArrayList<String>();

        switch (this.type) {

            case BOOLEAN:
                hints.add("true");
                hints.add("false");
                break;

            case STRING:
                hints.add("'Enter value'");
                break;

            case DATE:
                hints.add("'YYYY-MM-DD'");
                break;

            case DATETIME:
                hints.add("'YYYY-MM-DDTHH:mm:ss'");
                break;

            case BIG_DECIMAL:
            case FLOAT:
            case DOUBLE:
                hints.add("99.99");
                break;

            case INTEGER:
            case LONG:
                hints.add("99");
                break;
            default:
                break;
        }

        return hints;
    }

    public String getQueryEntityName() {
        return queryEntityName;
    }

    public void setQueryEntityName(String queryEntityName) {
        this.queryEntityName = queryEntityName;
    }

    public String getJoinQueryEntityName() {
        return joinQueryEntityName;
    }

    public void setJoinQueryEntityName(String joinQueryEntityName) {
        this.joinQueryEntityName = joinQueryEntityName;
    }

    public String convertToExternal(
            Object valueIn
    ) {
        if (valueIn == null) {
            return "";
        }

        if (getType() == QueryPropertyType.STRING) {
            return "'" + valueIn + "'";
        }

        if (isDate(getType())) {
            if (valueIn instanceof LocalDate) {
                return "'" + DateUtils.toString((LocalDate) valueIn) + "'";
            } else {
                return valueIn.toString();
            }
        }

        if (isDateTime(getType())) {
            if (valueIn instanceof LocalDateTime) {
                return "'" + DateUtils.toStringDateTime((LocalDateTime) valueIn) + "'";
            } else {
                return valueIn.toString();
            }
        }

        if (getType() == QueryPropertyType.BOOLEAN) {
            if (valueIn instanceof Boolean) {
                if ((Boolean) valueIn)
                    return "true";
                else
                    return "false";
            } else {
                return "'" + valueIn + "'";
            }
        }

        if (getType() == QueryPropertyType.BIG_DECIMAL) {
            return ((BigDecimal) valueIn).toPlainString();
        }

        return valueIn.toString();
    }

    public String convertToInline(
            Object valueIn
    ) {
        if (getType() == QueryPropertyType.STRING) {
            return "'" + valueIn.toString() + "'";
        }

        if (isDate(getType())) {
            if (valueIn instanceof LocalDate) {
                SimpleDateFormat sdf = new SimpleDateFormat(JDBC_DATE_FORMAT);
                return sdf.format(valueIn);
            } else {
                return valueIn.toString();
            }
        }

        if (isDateTime(getType())) {
            if (valueIn instanceof LocalDateTime) {
                return ((LocalDateTime) valueIn).format(DateTimeFormatter.ISO_DATE_TIME);
            } else if (valueIn instanceof ZonedDateTime) {
                return ((ZonedDateTime) valueIn).format(DateTimeFormatter.ISO_DATE_TIME);
            } else {
                return valueIn.toString();
            }
        }

        if (getType() == QueryPropertyType.BOOLEAN) {
            if (valueIn instanceof Boolean) {
                if (valueIn == Boolean.TRUE)
                    return "'" + "y" + "'";
                else
                    return "'" + "n" + "'";
            } else {
                return "'" + valueIn.toString() + "'";
            }
        }

        if (getType() == QueryPropertyType.BIG_DECIMAL) {
            return ((BigDecimal) valueIn).toPlainString();
        }

        return valueIn.toString();
    }

    public String convertToDatasetInline(Object valueIn) {
        if (isDate(getType())) {
            if (valueIn instanceof LocalDate) {
                return formatDate((LocalDate) valueIn);
            } else {
                return valueIn.toString();
            }
        }

        if (isDateTime(getType())) {
            if (valueIn instanceof LocalDateTime) {
                return formatDateTime((LocalDateTime) valueIn);
            } else {
                return valueIn.toString();
            }
        }

        return convertToInline(valueIn);
    }

    private String formatDateTime(LocalDateTime date) {
        SimpleDateFormat fmtr = new SimpleDateFormat(JDBC_DATETIME_FORMAT);

        String dt = fmtr.format(date);
        return "'" + dt + "'dt";
    }

    private String formatDate(LocalDate date) {
        SimpleDateFormat fmtr = new SimpleDateFormat(JDBC_DATE_FORMAT);

        String dt = fmtr.format(date);
        return "'" + dt + "'d";
    }

    public Object convertValue(Object valueIn) {
        if (valueIn instanceof List) {
            List listItems = (List) valueIn;
            ArrayList<Object> convertedItems = new ArrayList<>();
            for (Object item : listItems) {
                convertedItems.add(convertValue(item));
            }
            return convertedItems;
        }

        if (getType() == QueryPropertyType.STRING) {
            return convertEnumNameIfRequired(valueIn.toString());
        }

        if (getType() == QueryPropertyType.LONG) {
            if (valueIn instanceof Double) {
                return ((Double) valueIn).longValue();
            }

            return Long.valueOf(valueIn.toString());
        }

        if (isDate(getType())) {
            if (valueIn instanceof java.sql.Date) {
                java.sql.Date sqlDate = (java.sql.Date) valueIn;
                return DateUtils.parseDateFromMillis(sqlDate.getTime());
            } else if (valueIn instanceof java.sql.Timestamp) {
                java.sql.Timestamp sqlTimeStamp = (java.sql.Timestamp) valueIn;
                return DateUtils.parseDateTimeFromMillis(sqlTimeStamp.getTime());
            } else if (valueIn instanceof String) {
                return DateUtils.parseDateIgnoreTime((String) valueIn);
            }
        }

        if (isDateTime(getType())) {
            if (valueIn instanceof java.sql.Date) {
                java.sql.Date sqlDate = (java.sql.Date) valueIn;
                return DateUtils.parseDateTimeFromMillis(sqlDate.getTime());
            } else if (valueIn instanceof java.sql.Timestamp) {
                java.sql.Timestamp sqlTimeStamp = (java.sql.Timestamp) valueIn;
                return DateUtils.parseDateTimeFromMillis(sqlTimeStamp.getTime());
            } else if (valueIn instanceof String) {
                return DateUtils.parseFormulaDateTimeMillis((String) valueIn);
            }
        }

        if (getType() == QueryPropertyType.BOOLEAN) {
            if (valueIn instanceof String) {
                return Boolean.valueOf((String) valueIn);
            }

            if (valueIn instanceof Integer) {
                int b = (Integer) valueIn;
                return b == 1;
            }

            if (valueIn instanceof Boolean) {
                return valueIn;
            }
        }

        if (getType() == QueryPropertyType.INTEGER) {
            if (valueIn instanceof Integer) {
                return valueIn;
            }

            if (valueIn instanceof Double) {
                return ((Double) valueIn).intValue();
            }

            if (valueIn instanceof String) {
                return Integer.valueOf((String) valueIn);
            }

            if (valueIn instanceof BigDecimal) {
                BigDecimal bg = (BigDecimal) valueIn;
                return bg.intValue();
            }
        }

        if (getType() == QueryPropertyType.BIG_DECIMAL) {
            if (valueIn instanceof Double) {
                return BigDecimal.valueOf((Double) valueIn);
            }

            if (valueIn instanceof Integer) {
                return BigDecimal.valueOf((Integer) valueIn);
            }

            if (valueIn instanceof BigDecimal) {
                return valueIn;
            }
        }

        return valueIn;
    }

    public boolean isValueAnEnum(
            String value
    ) {
        if (!isEnum()) {
            return false;
        }

        return convertNameToCodeFunction.apply(value) != null;
    }

    private String convertEnumNameIfRequired(String value) {
        if (isEnum()) {
            String convertedValue = convertNameToCodeFunction.apply(value);
            if (convertedValue == null) {
                logger.error("Supplied enum value: " + value + " is not a valid code or name");
                throw new ApplicationRuntimeException(TransactionErrorCode.INVALID_QUERY_PROPERTY);
            }
            return convertedValue;
        }
        return value;
    }

    public String toString() {
        return ENTITY_PREFIX + name;
    }

    public void lookUpType(
            String propertyTypeAsStr
    ) {
        this.type = QueryPropertyType.valueOf(propertyTypeAsStr);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(
            Object obj
    ) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        QueryProperty other = (QueryProperty) obj;

        if (name == null) {
            return other.name == null;
        }
        return name.equals(other.name);
    }

    public boolean contains(String prefix) {
        if (label.length() < prefix.length())
            return false;

        String labelLC = label.toLowerCase();

        return labelLC.contains(prefix.toLowerCase());
    }

    @Override
    public int compareTo(QueryProperty o) {
        return this.label.compareTo(o.label);
    }

    private boolean isDateTime(QueryPropertyType queryPropertyType) {
        switch (queryPropertyType) {
            case DATETIME:
            case DATETIME_TIMEZONE_SENSITIVE:
                return true;
            default:
                return false;
        }
    }

    private boolean isDate(QueryPropertyType queryPropertyType) {
        switch (queryPropertyType) {
            case DATE:
            case DATE_TIMEZONE_SENSITIVE:
                return true;
            default:
                return false;
        }
    }
}