package com.sss.app.core.codes.snapshot;

import com.sss.app.core.entity.snapshot.AbstractErrorSnapshot;
import com.sss.app.core.snapshot.BaseSnapshotCollection;

import java.util.List;

public class CodeItemCollection extends BaseSnapshotCollection<CodeItem> {

    public static String itemType = "application/vnd.sss.codes";

    private String codeFamily;

    public CodeItemCollection() {
    }

    public CodeItemCollection(AbstractErrorSnapshot error) {
        super(error);
    }


    public CodeItemCollection(
            String codeFamily,
            List<CodeItem> items,
            long start,
            int limit,
            long totalItems) {
        super(
                start,
                totalItems,
                limit,
                items);
        this.codeFamily = codeFamily;
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    public String getCodeFamily() {
        return codeFamily;
    }

    public void setCodeFamily(String codeFamily) {
        this.codeFamily = codeFamily;
    }


}
