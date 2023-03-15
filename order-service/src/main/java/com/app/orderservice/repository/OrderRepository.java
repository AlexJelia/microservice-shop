package com.app.orderservice.repository;

import com.app.orderservice.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
}
