package com.sss.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Configuration
public class JsonMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        SimpleModule module = new SimpleModule("appModule", new Version(1, 1, 0, "snapshot", "sss", "app"));

        module.addSerializer(LocalDate.class, new LocalDateJsonSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateJsonDeserializer());

        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeJsonSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeJsonDeserializer());

        mapper.registerModule(module);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("ExplicitlyNullFilter", new EndsWithFilter("ExplicitlyNull"));
        mapper.setFilterProvider(filterProvider);

        return mapper;
    }
}
