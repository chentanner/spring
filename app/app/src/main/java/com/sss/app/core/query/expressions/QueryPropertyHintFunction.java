package com.sss.app.core.query.expressions;

import java.util.List;

public interface QueryPropertyHintFunction {
    public List<String> getHints(List<Object> parameters);
}
