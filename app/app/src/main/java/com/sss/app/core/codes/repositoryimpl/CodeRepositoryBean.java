package com.sss.app.core.codes.repositoryimpl;

import com.sss.app.core.codes.model.CodeLocale;
import com.sss.app.core.codes.repository.CodeRepository;
import com.sss.app.core.entity.managers.ApplicationContextFactory;
import com.sss.app.core.entity.repository.BaseRepository;
import org.springframework.stereotype.Component;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = CodeRepositoryBean.FIND_DEFAULT_LOCALE,
                query = "SELECT codeLocale " +
                        "FROM CodeLocale codeLocale " +
                        "WHERE codeLocale.isDefaultLanguageLocale = true " +
                        "ORDER BY codeLocale.localeCode")
})
@Component
public class CodeRepositoryBean implements CodeRepository {
    public static final String FIND_DEFAULT_LOCALE = "find_default_locale";


    public CodeLocale load(String locale) {
        return getBaseDAO().loadNonEntity(CodeLocale.class, locale);
    }

    public CodeLocale findDefaultLocale() {
        List<CodeLocale> codeLocales = getBaseDAO().executeQuery(CodeLocale.class,
                                                                 FIND_DEFAULT_LOCALE);
        return codeLocales.iterator().next();
    }


    @Transient
    protected static BaseRepository getBaseDAO() {
        return ApplicationContextFactory.getBean(BaseRepository.class);
    }

}
