package com.app.orderservice.service;

import com.app.dto.OrchestratorResponseDto;
import com.app.enums.OrderStatus;
import com.app.orderservice.model.Order;
import com.app.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderEventUpdateService {

    @Autowired
    private OrderRepository repository;

    @Transactional
    public void updateOrder(final OrchestratorResponseDto responseDTO){
        Order order = repository.findById(responseDTO.getOrderId()).orElseThrow(IllegalArgumentException::new);
        order.setOrderStatus(responseDTO.getStatus());
        if(order.getOrderStatus().equals(OrderStatus.ORDER_COMPLETED)){
            repository.save(order);
            log.info("Order completed!!!");
            //send message to notification service
        }else{
            repository.save(order);
            log.info("Order canceled");
            //send message to notification service
        }
    }
}