package com.sss.app.core.query.propertymapper;

import com.sss.app.dinos.snapshot.DinoQueryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryPropertyMapperConfig {

    @Bean
    public QueryPropertyMapper queryPropertyMapper() {

        BaseQueryPropertyMapper mapper = new BaseQueryPropertyMapper();
        addMappings(mapper);

        return mapper;
    }

    protected void addMappings(BaseQueryPropertyMapper mapper) {
        mapper.addQueryPropertyMap(new DinoQueryProperties());
    }
}
