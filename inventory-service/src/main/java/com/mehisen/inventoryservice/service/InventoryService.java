package com.mehisen.inventoryservice.service;

import com.mehisen.inventoryservice.dto.InventoryResponse;
import com.mehisen.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor

public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream().map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode()).isInSock(inventory.getQuantity() > 0).build()).toList();
    }
}
