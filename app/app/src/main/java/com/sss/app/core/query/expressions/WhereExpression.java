package com.sss.app.core.query.expressions;

import java.io.Serializable;
import java.util.List;

public class WhereExpression extends ExpressionElement implements Serializable {
    private static final long serialVersionUID = 1L;

    private int expressionIndex;
    private QueryProperty queryProperty;
    private Object propertyValue;
    private String propertyAlias;
    private String joinAlias;
    private ExpressionOperator operator;
    private boolean ignoreCaseFlag = false;

    public WhereExpression() {
        super("WHERE");
    }

    public WhereExpression(
            QueryProperty propertyName,
            ExpressionOperator operator,
            Object propertyValue) {

        super("WHERE");
        this.queryProperty = propertyName;
        this.operator = operator;
        this.propertyValue = handleLikeOperators(operator, propertyValue);
    }

    public WhereExpression(
            String joinAlias,
            QueryProperty propertyName,
            ExpressionOperator operator,
            Object propertyValue) {

        super("WHERE");
        this.joinAlias = joinAlias;
        this.queryProperty = propertyName;
        this.operator = operator;
        this.propertyValue = handleLikeOperators(operator, propertyValue);
    }

    private Object handleLikeOperators(
            ExpressionOperator operator,
            Object propertyValue) {

        if (propertyValue == null)
            return null;

        if (propertyValue instanceof String) {
            String stringProperty = (String) propertyValue;
            if (operator == ExpressionOperator.CONTAINS)
                return "%" + stringProperty + "%";
            else if (operator == ExpressionOperator.START_LIKE)
                return stringProperty + "%";
            else
                return propertyValue;
        } else {
            return propertyValue;
        }
    }

    public int getExpressionIndex() {
        return expressionIndex;
    }

    public void setExpressionIndex(int expressionIndex) {
        this.expressionIndex = expressionIndex;
    }

    public QueryProperty getQueryProperty() {
        return queryProperty;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public ExpressionOperator getOperator() {
        return operator;
    }

    public String toExternalString() {
        StringBuilder buffer = new StringBuilder();

        if (ignoreCaseFlag && QueryPropertyType.STRING.equals(queryProperty.getType())) {
            if (hasJoinAlias())
                buffer.append("lower(" + joinAlias + "." + queryProperty.getLabel() + ")");
            else
                buffer.append("lower(" + queryProperty.getLabel() + ")");
        } else {
            if (hasJoinAlias())
                buffer.append(joinAlias + "." + queryProperty.getLabel());
            else
                buffer.append(queryProperty.getLabel());
        }

        buffer.append(" ");

        if (operator == ExpressionOperator.CONTAINS || operator == ExpressionOperator.START_LIKE) {
            buffer.append(ExpressionOperator.LIKE.getExternalOperator());
        } else {
            buffer.append(operator.getExternalOperator());
        }

        buffer.append(" ");

        if (operator.equals(ExpressionOperator.IN) || operator.equals(ExpressionOperator.NOT_IN)) {
            buffer.append("(");
            if (propertyValue != null) {
                if (propertyValue instanceof List) {
                    List list = (List) propertyValue;
                    if (!list.isEmpty()) {
                        buffer.append(queryProperty.convertToExternal(list.get(0)));

                        for (int i = 1; i < list.size(); i++) {
                            buffer.append(",");
                            buffer.append(queryProperty.convertToExternal(list.get(i)));
                        }
                    }
                } else {
                    buffer.append(queryProperty.convertToExternal(propertyValue));
                }
            }
            buffer.append(") ");
        } else {
            buffer.append(queryProperty.convertToExternal(propertyValue));
        }

        return buffer.toString();
    }

    public String toFormattedString() {
        StringBuilder buffer = new StringBuilder();

        if (ignoreCaseFlag && QueryPropertyType.STRING.equals(queryProperty.getType())) {
            if (hasJoinAlias())
                buffer.append("lower(" + joinAlias + "." + queryProperty.getLabel() + ")");
            else
                buffer.append("lower(" + queryProperty.getLabel() + ")");
        } else {
            if (hasJoinAlias())
                buffer.append(joinAlias + "." + queryProperty.getLabel());
            else
                buffer.append(queryProperty.getLabel());
        }

        buffer.append(" ");

        if (operator == ExpressionOperator.CONTAINS || operator == ExpressionOperator.START_LIKE) {
            buffer.append(ExpressionOperator.LIKE.getOperator());
        } else {
            buffer.append(operator.getOperator());
        }

        buffer.append(" ");

        if (operator.equals(ExpressionOperator.IN) || operator.equals(ExpressionOperator.NOT_IN)) {
            buffer.append("(");
            if (propertyValue != null) {
                if (propertyValue instanceof List) {
                    List list = (List) propertyValue;
                    if (!list.isEmpty()) {
                        buffer.append(list.get(0).toString());

                        for (int i = 1; i < list.size(); i++) {
                            buffer.append(",");
                            buffer.append(list.get(i).toString());
                        }
                    }
                } else {
                    buffer.append(propertyValue);
                }
            }
            buffer.append(") ");
        } else {
            buffer.append(queryProperty.convertToInline(propertyValue));
        }


        return buffer.toString();
    }


    public String toString() {
        StringBuilder buffer = new StringBuilder();

        if (ignoreCaseFlag && QueryPropertyType.STRING.equals(queryProperty.getType())) {
            if (hasJoinAlias())
                buffer.append("lower(" + joinAlias + "." + queryProperty.getName() + ")");
            else
                buffer.append("lower(" + queryProperty.toString() + ")");
        } else {
            if (hasJoinAlias())
                buffer.append(joinAlias + "." + queryProperty.getName());
            else
                buffer.append(queryProperty.toString());
        }

        buffer.append(" ");
        if (operator == ExpressionOperator.CONTAINS || operator == ExpressionOperator.START_LIKE) {
            buffer.append(ExpressionOperator.LIKE.getOperator());
        } else {
            buffer.append(operator.getOperator());
        }
        buffer.append(" ");

        if (operator.equals(ExpressionOperator.IN) || operator.equals(ExpressionOperator.NOT_IN))
            buffer.append("(");
        if (propertyValue != null) {
            buffer.append(":");
            buffer.append(propertyAlias);
            buffer.append(" ");
        }
        if (operator.equals(ExpressionOperator.IN) || operator.equals(ExpressionOperator.NOT_IN))
            buffer.append(") ");


        return buffer.toString();
    }


    public void setQueryProperty(QueryProperty queryProperty) {
        this.queryProperty = queryProperty;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setOperator(ExpressionOperator operator) {
        this.operator = operator;
    }

    public boolean hasJoinAlias() {
        return joinAlias != null;
    }

    public String getJoinAlias() {
        return joinAlias;
    }

    public void setJoinAlias(String joinAlias) {
        this.joinAlias = joinAlias;
    }

    public String getPropertyAlias() {
        return propertyAlias;
    }

    public void setPropertyAlias(String alias) {
        this.propertyAlias = alias;
    }

    public Boolean getIgnoreCaseFlag() {
        return ignoreCaseFlag;
    }


    public void setIgnoreCaseFlag(Boolean ignoreCaseFlag) {
        this.ignoreCaseFlag = ignoreCaseFlag;
    }


}