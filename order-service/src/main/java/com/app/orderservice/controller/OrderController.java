package com.app.orderservice.controller;

import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    public List<OrderResponse> getOrders() {
        return orderService.getAll();
    }
}
