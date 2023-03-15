package com.app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
public class OrchestratorRequestDto {
    private Integer userId;
    private String skuCode;
    private Integer quantity;
    private UUID orderId;
    private BigDecimal amount;
}
