package org.stockmaster3000.stockmaster3000.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;

/**
 * Service class for handling inventory-related operations.
 *
 * <p>Provides methods to create, update, delete, and fetch inventory data
 * associated with users.</p>
 */
@Service
public class InventoryService {

  private final UserRepository userRepository;
  private final InventoryRepository inventoryRepository;

  /**
   * Creates a new instance of {@code InventoryService} with the required repositories.
   *
   * @param userRepository the user repository
   * @param inventoryRepository the inventory repository
   */
  public InventoryService(UserRepository userRepository, InventoryRepository inventoryRepository) {
    this.userRepository = userRepository;
    this.inventoryRepository = inventoryRepository;
  }

  /**
   * Retrieves all inventories belonging to a specific user by their username.
   *
   * @param username the username of the user
   * @return a list of inventories for the given user
   */
  public List<Inventory> getAllInventoriesByUser(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return inventoryRepository.findByUser(user);
  }

  /**
   * Retrieves a single inventory by its ID.
   *
   * @param inventoryId the ID of the inventory
   * @return an {@code Optional} containing the inventory if found
   */
  public Optional<Inventory> getInventoryById(Long inventoryId) {
    return inventoryRepository.findById(inventoryId);
  }

  /**
   * Adds a new inventory and associates it with a user.
   *
   * @param inventoryName the name of the new inventory
   * @param username the username of the user to associate with the inventory
   * @return the newly created and saved inventory
   */
  public Inventory addInventory(String inventoryName, String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Inventory newInventory = new Inventory();
    newInventory.setName(inventoryName);
    newInventory.setUser(user);
    return inventoryRepository.save(newInventory);
  }

  /**
   * Updates an existing inventory.
   *
   * @param inventory the inventory to update
   * @return the updated inventory
   */
  public Inventory updateInventory(Inventory inventory) {
    return inventoryRepository.save(inventory);
  }

  /**
   * Deletes an inventory by its ID.
   *
   * @param inventoryId the ID of the inventory to delete
   */
  public void deleteInventory(Long inventoryId) {
    inventoryRepository.deleteById(inventoryId);
  }

  /**
   * Deletes an inventory by its object reference.
   *
   * @param inventory the inventory to delete
   */
  public void deleteInventory(Inventory inventory) {
    inventoryRepository.delete(inventory);
  }

  /**
   * Retrieves all inventory names for a specific user.
   *
   * @param username the username of the user
   * @return a list of inventory names for the given user
   */
  public List<String> getAllInventoryNamesByUser(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return inventoryRepository.findByUser(user)
        .stream()
        .map(Inventory::getName)
        .toList();
  }

  /**
   * Finds an inventory by its ID.
   *
   * @param id the ID of the inventory
   * @return an {@code Optional} containing the inventory if found
   */
  public Optional<Inventory> findById(Long id) {
    return inventoryRepository.findById(id);
  }
}
