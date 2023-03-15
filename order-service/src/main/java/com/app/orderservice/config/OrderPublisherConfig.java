package com.app.orderservice.config;

import com.app.dto.OrchestratorRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderPublisherConfig {

    @Bean
    public Sinks.Many<OrchestratorRequestDto> orderSinks(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<OrchestratorRequestDto>> orderSupplier(Sinks.Many<OrchestratorRequestDto> sinks){
       return sinks::asFlux;
    }
}
