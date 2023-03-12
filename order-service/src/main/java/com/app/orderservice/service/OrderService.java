package com.app.orderservice.service;

import com.app.orderservice.dto.InventoryResponse;
import com.app.orderservice.dto.OrderLineItemsDto;
import com.app.orderservice.dto.OrderRequest;
import com.app.orderservice.event.OrderPlacedEvent;
import com.app.orderservice.model.Order;
import com.app.orderservice.model.OrderLineItems;
import com.app.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::asDto)
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItems)
                .build();
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //Implement own span to trace this part of code
        //Spring Cloud Sleuth will assign this spanId to below code
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
            //make request like
            //http://localhost:8082/api/inventory/check?skuCode=item1&skuCode=item2
            //retrieve from response array of InventoryResponse objects
            //which contain boolean isInStock
            log.info("Calling inventory service");
            InventoryResponse[] responseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory/check",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            Objects.requireNonNull(responseArray);
            if (responseArray.length == 0) {
                throw new IllegalArgumentException("No one product is in Stock");
            }
            boolean areProductsInStock = Arrays.stream(responseArray).allMatch(InventoryResponse::isInStock);

            if (areProductsInStock) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
                return "Order Placed Successfully";
            } else if (skuCodes.size() == responseArray.length) {
                List<InventoryResponse> notInStock = Arrays.stream(responseArray).filter(item -> !item.isInStock()).toList();
                throw new IllegalArgumentException(notInStock + "not in stock,please,try again later");
            } else {
                throw new IllegalArgumentException("Some of products do not exist,check your request");
            }
        } finally {
            inventoryServiceLookup.end();
        }
    }

    private OrderLineItems asDto(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }
}
