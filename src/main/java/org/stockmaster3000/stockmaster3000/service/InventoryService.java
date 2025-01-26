package org.stockmaster3000.stockmaster3000.service;

import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void addStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setStock(inventory.getStock() + quantity);
        inventoryRepository.save(inventory);
    }

    public void reduceStock(Long productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        if (inventory.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }
        inventory.setStock(inventory.getStock() - quantity);
        inventoryRepository.save(inventory);
    }
}
