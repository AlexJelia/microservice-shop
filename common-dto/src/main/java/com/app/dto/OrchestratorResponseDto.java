package com.app.dto;

import com.app.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
public class OrchestratorResponseDto {
    private Integer userId;
    private String skuCode;
    private UUID orderId;
    private BigDecimal amount;
    private OrderStatus status;
}
