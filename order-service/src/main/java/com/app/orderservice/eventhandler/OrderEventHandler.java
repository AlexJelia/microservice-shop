package com.app.orderservice.eventhandler;

import com.app.dto.OrchestratorResponseDto;
import com.app.orderservice.service.OrderEventUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class OrderEventHandler {

    private final OrderEventUpdateService service;

    @Bean
    public Consumer<OrchestratorResponseDto> eventConsumer(){
        //listen order-updated-topic
        //will update order
        return (orchestratorResponse)-> service.updateOrder(orchestratorResponse);
    }

}
