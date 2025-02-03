package org.stockmaster3000.stockmaster3000.service;

import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;

    public InventoryService(UserRepository userRepository, InventoryRepository inventoryRepository) {
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // Get all inventories for a specific user by username
    public List<Inventory> getAllInventoriesByUser(String username) {
        // Retrieve the user from the repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));  // Handle user not found

        // Fetch and return the inventories for that user
        return inventoryRepository.findByUser(user);
    }

    // Get a single inventory by ID
    public Optional<Inventory> getInventoryById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    // Add a new inventory
    // Add a new inventory, associating it with the currently authenticated user
    public Inventory addInventory(String inventoryName, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Inventory newInventory = new Inventory();
        newInventory.setName(inventoryName);
        newInventory.setUser(user); // Associate the inventory with the provided user
        return inventoryRepository.save(newInventory);
    }

    // Update an existing inventory
    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Delete an inventory by ID
    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    // Get all inventory names by User object (returns List of inventory names for a user)
    public List<String> getAllInventoryNamesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return inventoryRepository.findByUser(user)
                .stream()
                .map(Inventory::getName)
                .toList();
    }
}
