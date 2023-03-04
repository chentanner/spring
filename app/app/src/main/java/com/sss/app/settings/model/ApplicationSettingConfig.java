package com.sss.app.settings.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationSettingConfig {

    @Primary
    @Bean
    public ApplicationSettingManager applicationSettingManager() {
        return new ApplicationSettingManagerBean();
    }

}
