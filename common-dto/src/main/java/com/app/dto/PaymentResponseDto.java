package com.app.dto;

import com.app.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
    private Integer userId;
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus status;
}
