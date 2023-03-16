package com.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("payment")
    public WebClient paymentClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean
    @Qualifier("inventory")
    public WebClient inventoryClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

}