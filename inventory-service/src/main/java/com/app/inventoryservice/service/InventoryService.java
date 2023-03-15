package com.app.inventoryservice.service;

import com.app.dto.InventoryRequest;
import com.app.dto.InventoryResponse;
import com.app.enums.InventoryStatus;
import com.app.inventoryservice.model.Inventory;
import com.app.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public InventoryResponse deductInventory(final InventoryRequest requestDTO) {
        log.info("Checking Inventory");
        InventoryResponse response = InventoryResponse.builder()
                .orderId(requestDTO.getOrderId())
                .skuCode(requestDTO.getSkuCode())
                .build();
        Inventory item = inventoryRepository.findBySkuCode(requestDTO.getSkuCode()).orElse(null);
        if(item==null || item.getQuantity()>requestDTO.getQuantity()){
            response.setStatus(InventoryStatus.UNAVAILABLE);
        }else{
            item.setQuantity(item.getQuantity()-requestDTO.getQuantity());
            inventoryRepository.save(item);
            response.setStatus(InventoryStatus.AVAILABLE);
        }
        return response;
    }

    @Transactional
    public void addInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public void revert(InventoryRequest inventoryRequest) {
       Inventory item = inventoryRepository.findBySkuCode(inventoryRequest.getSkuCode()).orElseThrow(IllegalArgumentException::new);
       item.setQuantity(inventoryRequest.getQuantity());
       inventoryRepository.save(item);
    }
}