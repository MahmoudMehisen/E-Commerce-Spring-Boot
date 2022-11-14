package com.mehisen.inventoryservice;

import com.mehisen.inventoryservice.model.Inventory;
import com.mehisen.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setQuantity(1000);
            inventory.setSkuCode("ip_red");

            Inventory inventory2 = new Inventory();
            inventory2.setQuantity(0);
            inventory2.setSkuCode("ip_green");

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory2);
        };
    }

}
