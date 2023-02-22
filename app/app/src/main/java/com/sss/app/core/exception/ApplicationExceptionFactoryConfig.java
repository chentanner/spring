package com.sss.app.core.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationExceptionFactoryConfig {

    @Bean
    public ApplicationExceptionFactory applicationExceptionFactory() {
        return new ApplicationExceptionFactoryBean();
    }

}
