package com.sss.app.core.entity.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public BaseRepository baseDAO() {
        BaseRepository baseRepo = new BaseRepository();

        return baseRepo;
    }
}
