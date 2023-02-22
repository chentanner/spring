package com.sss.app.core.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

public class QueryParameters implements Serializable, Iterable<QueryParameter> {
    private final List<QueryParameter> parameters = new ArrayList<>();

    public List<QueryParameter> getParameters() {
        return parameters;
    }

    public void add(String name, Object value) {
        parameters.add(new QueryParameter(name, value));

    }

    @Override
    public Iterator<QueryParameter> iterator() {
        return parameters.iterator();
    }

    @Override
    public Spliterator<QueryParameter> spliterator() {
        return parameters.spliterator();
    }
}
