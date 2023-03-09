package com.sss.app.core.query.expressions;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "elementTypeValue")
@JsonSubTypes({
        @Type(value = WhereExpression.class, name = "WHERE"),
        @Type(value = ConnectionElement.class, name = "CONNECTION"),
        @Type(value = BracketElement.class, name = "BRACKET"),
})
public abstract class ExpressionElement {
    private String elementTypeValue;

    public abstract String toFormattedString();

    protected ExpressionElement(String elementTypeValue) {
        super();
        this.elementTypeValue = elementTypeValue;
    }

    public String getElementTypeValue() {
        return elementTypeValue;
    }

    public void setElementTypeValue(String elementTypeValue) {
        this.elementTypeValue = elementTypeValue;
    }

    public Object createDatasetInline() {
        return toFormattedString();
    }

    public String toString() {
        return toFormattedString();
    }

    protected abstract String toExternalString();
}