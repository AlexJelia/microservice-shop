package com.app.inventoryservice;

import com.app.inventoryservice.model.Inventory;
import com.app.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory item = new Inventory();
            item.setSkuCode("ps4");
            item.setQuantity(100);

            Inventory item1 = new Inventory();
            item1.setSkuCode("router");
            item1.setQuantity(0);

            Inventory item2 = new Inventory();
            item2.setSkuCode("iphone");
            item2.setQuantity(12);

            inventoryRepository.save(item);
            inventoryRepository.save(item1);
            inventoryRepository.save(item2);
        };
    }
}
