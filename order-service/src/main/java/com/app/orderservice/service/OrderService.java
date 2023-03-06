package com.app.orderservice.service;

import com.app.orderservice.dto.InventoryResponse;
import com.app.orderservice.dto.OrderLineItemsDto;
import com.app.orderservice.dto.OrderRequest;
import com.app.orderservice.model.Order;
import com.app.orderservice.model.OrderLineItems;
import com.app.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

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

        //make request like
        //http://localhost:8082/api/inventory?skuCode=item1&skuCode=item2
        //retrieve from response array of InventoryResponse objects
        //which contain boolean isInStock
        InventoryResponse[] responseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class) //here request is async
                .block();  //to make sync
        boolean areProductsInStock = Arrays.stream(responseArray).allMatch(InventoryResponse::isInStock);
        if(areProductsInStock) {
            orderRepository.save(order);
            return "Order Placed Successfully";
        }else {
            throw new IllegalArgumentException("Product is not in stock");
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
