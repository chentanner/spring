package com.sss.app.core.query.expressions;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConnectionElement extends ExpressionElement {

    private String connectorCode;

    public ConnectionElement(ExpressionConnector connector) {
        super("CONNECTION");
        this.connectorCode = connector.getConnector();
    }

    @JsonIgnore
    public ExpressionConnector getConnector() {
        return ExpressionConnector.lookup(connectorCode);
    }


    public String getConnectorCode() {
        return connectorCode;
    }

    public void setConnectorCode(String connectorCode) {
        this.connectorCode = connectorCode;
    }

    @Override
    public String toFormattedString() {
        return connectorCode;
    }

    @Override
    protected String toExternalString() {
        return connectorCode;
    }


}
