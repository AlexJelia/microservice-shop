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
                .baseUrl("http://payment-service/api/inventory")
                .build();
    }

    @Bean
    @Qualifier("inventory")
    public WebClient inventoryClient(){
        return WebClient.builder()
                .baseUrl("http://inventory-service/api/inventory")
                .build();
    }

}