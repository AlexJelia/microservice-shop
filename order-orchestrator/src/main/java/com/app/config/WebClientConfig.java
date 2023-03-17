package com.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    @Qualifier("payment")
    public WebClient.Builder paymentClient(){
        return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    @Qualifier("inventory")
    public WebClient.Builder inventoryClient(){
        return WebClient.builder();
    }
}