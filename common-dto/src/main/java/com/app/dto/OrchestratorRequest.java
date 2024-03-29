package com.app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrchestratorRequest {
    private Integer userId;
    private String skuCode;
    private Integer quantity;
    private BigDecimal amount;
    private UUID orderId;
}
