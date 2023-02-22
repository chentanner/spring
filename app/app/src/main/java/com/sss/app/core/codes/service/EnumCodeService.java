package com.sss.app.core.codes.service;

import com.sss.app.core.codes.snapshot.CodeItem;

import java.util.List;

public interface EnumCodeService {
    public static final String BEAN_NAME = "enumCodeService";

    public List<String> getCodeFamilies();

    public List<CodeItem> fetchCodeItems(String codeFamily);

    public List<CodeItem> fetchCodeItems(String codeFamily, String locale);

    public List<CodeItem> fetchCodeItems(String codeFamily, String locale, String filter);

    public CodeItem fetchCodeItem(String codeFamily, String code);

    public CodeItem fetchCodeItemByLabel(String codeFamily, String label);
}
