package org.stockmaster3000.stockmaster3000.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.model.Inventory;

public class InventoryService {
    @Autowired
    private InventoryService inventoryService;

    // Fetching inventory name
    public String getInventoryName(Long inventoryId) {
        return inventoryService.getInventoryName(inventoryId);
    }

    // Fetching all of the inventories of the user
    public List<Inventory> getAllInventoriesByUserId(Long userId) {
        return inventoryService.getAllInventoriesByUserId(userId);
    } 

    // Deleting inventory by ID
    public void deleteInventoryById(Long inventoryId) {
        inventoryService.deleteInventoryById(inventoryId);
    }
}
