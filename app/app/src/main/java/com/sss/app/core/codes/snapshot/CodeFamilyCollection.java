package com.sss.app.core.codes.snapshot;


import com.sss.app.core.snapshot.BaseSnapshotCollection;
import com.sss.app.core.snapshot.ErrorSnapshot;

import java.util.List;

public class CodeFamilyCollection extends BaseSnapshotCollection<String> {

    public static String itemType = "application/vnd.sss.codeFamily";

    public CodeFamilyCollection() {
    }

    public CodeFamilyCollection(ErrorSnapshot errorCode) {
        super(errorCode);
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    public CodeFamilyCollection(
            List<String> items,
            long start,
            int limit,
            long totalItems) {
        super(
                start,
                totalItems,
                limit,
                items);
    }


}
