package com.sss.app.core.query.parsing.token;

import com.sss.app.core.query.parsing.ParsingQueryNode;

import java.util.ArrayList;
import java.util.List;

public class QueryInListToken extends AbstractQueryNode {

    private final List<ParsingQueryNode> nodes = new ArrayList<>();

    public void addInParameter(ParsingQueryNode param) {
        nodes.add(param);
    }


    public List<ParsingQueryNode> getList() {
        return nodes;
    }

    @Override
    public void reset() {
        super.reset();
        for (ParsingQueryNode node : nodes)
            node.reset();
    }
}