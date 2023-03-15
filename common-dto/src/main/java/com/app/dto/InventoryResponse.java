package com.app.dto;

import com.app.enums.InventoryStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InventoryResponse {
    private UUID orderId;
    private String skuCode;
    private InventoryStatus status;
}
