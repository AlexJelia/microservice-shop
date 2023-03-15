package com.app.orderservice.controller;

import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public String createOrder(@RequestBody OrderRequest orderRequest){
        log.info("creating order");
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/all")
    public List<OrderResponse> getOrders(){
        return orderService.getAll();
    }
}
