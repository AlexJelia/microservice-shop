package com.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InventoryRequest {
    private String skuCode;
    private Integer quantity;
    private UUID orderId;
}
