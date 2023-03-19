package com.app.orderservice.config;

import com.app.dto.OrchestratorRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderPublisherConfig {

    @Bean
    public Sinks.Many<OrchestratorRequest> orderSinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<OrchestratorRequest>> orderSupplier(Sinks.Many<OrchestratorRequest> sinks) {
        return sinks::asFlux;
    }
}
