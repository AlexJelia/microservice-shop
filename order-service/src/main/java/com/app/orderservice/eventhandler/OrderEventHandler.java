package com.app.orderservice.eventhandler;

import com.app.dto.OrchestratorRequestDto;
import com.app.dto.OrchestratorResponseDto;
import com.app.orderservice.service.OrderEventUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class OrderEventHandler {

    private final Flux<OrchestratorRequestDto> flux;

    private final OrderEventUpdateService service;

    @Bean
    public Supplier<Flux<OrchestratorRequestDto>> supplier(){
        return () -> flux;
    };

    @Bean
    public Consumer<Flux<OrchestratorResponseDto>> consumer(){
        return f -> f
                .doOnNext(c -> System.out.println("Consuming :: " + c))
                .flatMap(responseDTO -> this.service.updateOrder(responseDTO))
                .subscribe();
    };

}