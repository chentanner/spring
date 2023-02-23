package com.sss.app.core.codes.repository;

import com.sss.app.core.codes.model.CodeLocale;

public interface CodeRepository {
    public CodeLocale load(String locale);

    public CodeLocale findDefaultLocale();
}
