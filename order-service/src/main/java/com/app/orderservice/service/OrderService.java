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
    private Sinks.Many<OrchestratorRequestDto> orderSinks;


    public void publishOrderEvent(OrderRequest orderRequest){
        OrchestratorRequestDto orderEvent= getOrchestratorRequest(orderRequest);
        orderSinks.tryEmitNext(orderEvent);
    }


    @Transactional
    public String createOrder(OrderRequest orderRequest){
         orderRepository.save(dtoToEntity(orderRequest));
         publishOrderEvent(orderRequest);
         return "Order Created";
    }
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
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
