package com.sss.app.core.query.parsing;

public interface ParsingQueryNode extends QueryNode {

    public boolean isWasProcessed();

    public void setWasProcessed(boolean wasProcessed);

    public void reset();
}