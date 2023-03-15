package com.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderRequest {
    private Integer userId;
    private String skuCode;
    private Integer quantity;
    private BigDecimal price;
    private UUID orderId;
}
