package com.ottention.banana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Configuration
public class AppConfig {

    @Bean
    public WebClient kakao() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    @Bean
    public WebClient google() {
        return WebClient.builder()
                .baseUrl("https://oauth2.googleapis.com")
                .defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

}
