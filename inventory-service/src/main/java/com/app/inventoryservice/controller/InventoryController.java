package com.app.inventoryservice.controller;

import com.app.dto.InventoryRequest;
import com.app.dto.InventoryResponse;
import com.app.inventoryservice.model.Inventory;
import com.app.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    @PostMapping("/deduct")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse deduct(@RequestBody final InventoryRequest requestDTO){
        return this.service.deductInventory(requestDTO);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody InventoryRequest inventoryRequest){
        this.service.addInventory(inventoryRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAll() {
        return service.getAll();
    }

    @PostMapping("/revert")
    @ResponseStatus(HttpStatus.OK)
    public void revert(@RequestBody InventoryRequest inventoryRequest) {
        service.revert(inventoryRequest);
    }

}
