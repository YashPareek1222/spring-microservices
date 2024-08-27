package com.yash.inventory_service.service;

import com.yash.inventory_service.dto.InventoryResponse;
import com.yash.inventory_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findByskuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .inStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
