package com.sss.app.core.query.expressions;

import java.io.Serializable;

public class OrderExpression implements Serializable {
    private static final long serialVersionUID = 1L;

    private QueryProperty queryProperty;
    private OrderOperator operator = OrderOperator.ASCENDING;
    private boolean ignoreCaseFlag = false;

    public OrderExpression() {
    }

    public OrderExpression(OrderOperator operator, QueryProperty queryProperty) {
        super();
        this.operator = operator;
        this.queryProperty = queryProperty;
    }

    public OrderExpression(OrderOperator operator, QueryProperty queryProperty, boolean ignoreCaseFlag) {
        super();
        this.operator = operator;
        this.queryProperty = queryProperty;
        this.ignoreCaseFlag = ignoreCaseFlag;
    }

    public OrderExpression(QueryProperty queryProperty, boolean ignoreCaseFlag) {
        super();
        this.queryProperty = queryProperty;
        this.ignoreCaseFlag = ignoreCaseFlag;
    }

    public OrderExpression(QueryProperty queryProperty) {
        super();
        this.queryProperty = queryProperty;
    }

    public QueryProperty getQueryProperty() {
        return queryProperty;
    }

    public void setQueryProperty(QueryProperty queryProperty) {
        this.queryProperty = queryProperty;
    }

    public OrderOperator getOperator() {
        return operator;
    }

    public void setOperator(OrderOperator operator) {
        this.operator = operator;
    }

    public Boolean getIgnoreCaseFlag() {
        return ignoreCaseFlag;
    }

    public void setIgnoreCaseFlag(Boolean ignoreCaseFlag) {
        this.ignoreCaseFlag = ignoreCaseFlag;
    }

    public String toString() {
        if (ignoreCaseFlag &&
                QueryPropertyType.STRING.equals(queryProperty.getType())) {
            return "lower(" + queryProperty.toString() + ") " + operator.getOperator();
        } else {
            return queryProperty.toString() + " " + operator.getOperator();
        }
    }

    public String toFormattedString() {
        if (ignoreCaseFlag &&
                QueryPropertyType.STRING.equals(queryProperty.getType())) {
            return "LOWER(" + queryProperty.getLabel() + ") " + operator.getOperator();
        } else {
            return queryProperty.getLabel() + " " + operator.getOperator();
        }
    }

}
