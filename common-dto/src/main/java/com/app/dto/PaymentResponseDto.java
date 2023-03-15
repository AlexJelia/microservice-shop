package com.app.dto;

import com.app.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentResponseDto {
    private Integer userId;
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus status;
}
