package com.app.orderservice.service;

import com.app.dto.OrchestratorRequestDto;
import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.enums.OrderStatus;
import com.app.orderservice.model.Order;
import com.app.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    private final Sinks.Many<OrchestratorRequestDto> sink;

    @Transactional
    public Mono<Order> createOrder(OrderRequest orderRequest){
        return orderRepository.save(this.dtoToEntity(orderRequest))
                .doOnNext(e -> orderRequest.setOrderId(e.getId()))
                .doOnNext(e -> this.emitEvent(orderRequest));
    }
    public Flux<OrderResponse> getAll() {
        return this.orderRepository.findAll()
                .map(this::entityToDto);
    }

    private void emitEvent(OrderRequest orderRequest){
        this.sink.tryEmitNext(this.getOrchestratorRequest(orderRequest));
    }

    private Order dtoToEntity(final OrderRequest dto){
        return Order.builder()
                .id(UUID.randomUUID())
                .userId(dto.getUserId())
                .skuCode(dto.getSkuCode())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .orderStatus(OrderStatus.ORDER_CREATED)
                .build();
    }

    private OrderResponse entityToDto(final Order purchaseOrder){
        return OrderResponse.builder()
                .orderId(purchaseOrder.getId())
                .userId(purchaseOrder.getUserId())
                .skuCode(purchaseOrder.getSkuCode())
                .amount(purchaseOrder.getPrice())
                .status(purchaseOrder.getOrderStatus())
                .build();
    }

    public OrchestratorRequestDto getOrchestratorRequest(OrderRequest orderRequestDTO){
        return OrchestratorRequestDto.builder()
                .userId(orderRequestDTO.getUserId())
                .amount(orderRequestDTO.getPrice())
                .orderId(orderRequestDTO.getOrderId())
                .skuCode(orderRequestDTO.getSkuCode())
                .build();
    }

}
