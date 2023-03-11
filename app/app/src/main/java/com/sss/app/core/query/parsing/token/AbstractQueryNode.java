package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.ParsingQueryNode;

public class AbstractQueryNode implements ParsingQueryNode {

    private boolean wasProcessed = false;

    public boolean isWasProcessed() {
        return wasProcessed;
    }

    public void setWasProcessed(boolean wasProcessed) {
        this.wasProcessed = wasProcessed;
    }

    @Override
    public void reset() {
        wasProcessed = false;
    }
}
