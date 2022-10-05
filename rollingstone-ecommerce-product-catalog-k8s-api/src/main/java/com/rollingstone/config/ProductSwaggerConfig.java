package com.rollingstone.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductSwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("Rollingstone Ecommerce")
                .packagesToScan("com.rollingstone.spring.controller")
                .pathsToMatch("/**")
                .build();
    }
}
