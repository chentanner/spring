package com.sss.app.core.entity.repository;

import com.sss.app.core.entity.model.ExpiryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public BaseRepository baseDAO() {
        BaseRepository baseRepo = new BaseRepository();

        baseRepo.setDefaultExpiryPolicy(new ExpiryPolicy());
        return baseRepo;
    }
}
