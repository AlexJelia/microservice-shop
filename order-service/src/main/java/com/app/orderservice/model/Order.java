package com.app.orderservice.model;


import com.app.enums.OrderStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private UUID id;
    private Integer userId;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
    private OrderStatus orderStatus;
}