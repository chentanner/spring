package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderClause implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<OrderExpression> orderExpressions = new ArrayList<OrderExpression>();

    public void addOrderExpression(OrderExpression orderExpression) {
        orderExpressions.add(orderExpression);
    }

    public List<OrderExpression> getOrderExpressions() {
        return orderExpressions;
    }

    public void setOrderExpressions(ArrayList<OrderExpression> orderExpressions) {
        this.orderExpressions = orderExpressions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(" Order By ");

        if (orderExpressions.isEmpty())
            return "";

        Iterator<OrderExpression> itor = orderExpressions.iterator();
        sb.append(itor.next().toString());

        if (orderExpressions.size() > 1) {
            while (itor.hasNext()) {
                sb.append(", ");
                sb.append(itor.next().toString());
            }
        }
        return sb.toString();
    }

    public String toFormattedString() {
        StringBuilder sb = new StringBuilder(" ORDER BY ");

        if (orderExpressions.isEmpty())
            return "";

        Iterator<OrderExpression> itor = orderExpressions.iterator();
        sb.append(itor.next().toFormattedString());

        if (orderExpressions.size() > 1) {
            while (itor.hasNext()) {
                sb.append(", ");
                sb.append(itor.next().toFormattedString());
            }
        }

        return sb.toString();
    }

    @JsonIgnore
    public boolean hasOrderByCriteria() {
        return !orderExpressions.isEmpty();
    }

    public void copyFrom(OrderClause orderClause) {
        this.orderExpressions.addAll(orderClause.orderExpressions);
    }

    public void clear() {
        orderExpressions = new ArrayList<>();
    }
}
