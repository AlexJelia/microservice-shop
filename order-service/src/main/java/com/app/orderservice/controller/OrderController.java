package com.app.orderservice.controller;

import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.orderservice.model.Order;
import com.app.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Mono<Order> createOrder(@RequestBody Mono<OrderRequest> mono){
        return mono
                .flatMap(this.orderService::createOrder);
    }

    @GetMapping("/all")
    public Flux<OrderResponse> getOrders(){
        return this.orderService.getAll();
    }
}
