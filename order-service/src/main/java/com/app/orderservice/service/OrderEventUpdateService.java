package com.app.orderservice.service;

import com.app.dto.OrchestratorResponseDto;
import com.app.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class OrderEventUpdateService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public Mono<Void> updateOrder(final OrchestratorResponseDto responseDTO){
        return this.repository.findById(responseDTO.getOrderId())
                .doOnNext(p -> p.setOrderStatus(responseDTO.getStatus()))
                .flatMap(this.repository::save)
                .then();
    }

}