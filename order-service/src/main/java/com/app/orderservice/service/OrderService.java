package com.app.orderservice.service;

import com.app.dto.OrchestratorRequest;
import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.enums.OrderStatus;
import com.app.orderservice.model.Order;
import com.app.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final Sinks.Many<OrchestratorRequest> orderSinks;

    public void publishOrderEvent(OrderRequest orderRequest) {
        OrchestratorRequest orderEvent = getOrchestratorRequest(orderRequest);
        orderSinks.tryEmitNext(orderEvent);
    }

    @Transactional
    public String createOrder(OrderRequest orderRequest) {
        orderRequest.setOrderId(UUID.randomUUID());
        orderRepository.save(dtoToEntity(orderRequest));
        publishOrderEvent(orderRequest);
        return "Order Placed,check your mail(localhost:8025)";
    }

    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private Order dtoToEntity(final OrderRequest dto) {
        return Order.builder()
                .id(dto.getOrderId())
                .userId(dto.getUserId())
                .skuCode(dto.getSkuCode())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .orderStatus(OrderStatus.ORDER_PROCESSING)
                .build();
    }

    private OrderResponse entityToDto(final Order purchaseOrder) {
        return OrderResponse.builder()
                .orderId(purchaseOrder.getId())
                .userId(purchaseOrder.getUserId())
                .skuCode(purchaseOrder.getSkuCode())
                .amount(purchaseOrder.getPrice())
                .status(purchaseOrder.getOrderStatus())
                .build();
    }

    public OrchestratorRequest getOrchestratorRequest(OrderRequest orderRequestDTO) {
        return OrchestratorRequest.builder()
                .userId(orderRequestDTO.getUserId())
                .amount(orderRequestDTO.getPrice())
                .orderId(orderRequestDTO.getOrderId())
                .skuCode(orderRequestDTO.getSkuCode())
                .quantity(orderRequestDTO.getQuantity())
                .build();
    }
}
