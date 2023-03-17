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

    @Transactional
    public InventoryResponse deductInventory(final InventoryRequest requestDTO) {
        InventoryResponse response = InventoryResponse.builder()
                .skuCode(requestDTO.getSkuCode())
                .build();
        Inventory item = inventoryRepository.findBySkuCode(requestDTO.getSkuCode()).orElse(null);
        if(item==null || item.getQuantity()<requestDTO.getQuantity()){
            response.setStatus(InventoryStatus.UNAVAILABLE);
            log.info("Item Unavailable");
        }else{
            item.setQuantity(item.getQuantity()-requestDTO.getQuantity());
            inventoryRepository.save(item);
            response.setStatus(InventoryStatus.AVAILABLE);
            log.info("Item Available");
        }
        return response;
    }

    @Transactional
    public void addInventory(InventoryRequest inventoryRequest) {
        inventoryRepository.save(toEntity(inventoryRequest));
    }
    @Transactional(readOnly = true)
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public void revert(InventoryRequest inventoryRequest) {
        log.info("Revert Items");
       Inventory item = inventoryRepository.findBySkuCode(inventoryRequest.getSkuCode()).orElseThrow(IllegalArgumentException::new);
       item.setQuantity(item.getQuantity()+inventoryRequest.getQuantity());
       inventoryRepository.save(item);
    }

    private Inventory toEntity(InventoryRequest inventoryRequest) {
        return Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
    }
}