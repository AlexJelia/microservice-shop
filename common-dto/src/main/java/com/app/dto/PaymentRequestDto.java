package com.app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentRequestDto {
    private Integer userId;
    private UUID orderId;
    private BigDecimal amount;
}
