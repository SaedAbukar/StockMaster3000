package org.stockmaster3000.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;
import org.stockmaster3000.stockmaster3000.service.InventoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private InventoryRepository inventoryRepo;

    @InjectMocks
    private InventoryService inventoryService;

    private User user;
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        // Initialize a test user
        user = new User();
        user.setUsername("testUser");

        // Initialize a test inventory associated with the user
        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setName("Test Inventory");
        inventory.setUser(user);
    }

    @Test
    public void testGetInventoriesByUser_success() {
        // Mock the repository to return the user when searched by username
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        // Mock the repository to return the user's inventories
        when(inventoryRepo.findByUser(user)).thenReturn(List.of(inventory));

        // Call the method to fetch inventories
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser("testUser");

        // Ensure the returned list contains the expected inventory
        assertEquals(1, inventories.size());
        assertEquals("Test Inventory", inventories.get(0).getName());
    }

    @Test
    public void testGetInventoriesByUser_userNotFound() {
        // Mock the repository to return an empty result when searching for a non-existent user
        when(userRepo.findByUsername("invalidUser")).thenReturn(Optional.empty());

        // Verify that an exception is thrown when trying to fetch inventories for a non-existent user
        Exception exception = assertThrows(RuntimeException.class, () ->
                inventoryService.getAllInventoriesByUser("invalidUser"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetInventoryById_success() {
        // Mock the repository to return an inventory when searched by ID
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventory));

        // Call the method to fetch inventory by ID
        Optional<Inventory> result = inventoryService.getInventoryById(1L);

        // Ensure the inventory was found and matches the expected values
        assertTrue(result.isPresent());
        assertEquals("Test Inventory", result.get().getName());
    }

    @Test
    public void testGetInventoryById_notFound() {
        // Mock the repository to return an empty result when searching by a non-existent ID
        when(inventoryRepo.findById(2L)).thenReturn(Optional.empty());

        // Call the method and verify that no inventory is found
        Optional<Inventory> result = inventoryService.getInventoryById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testAddInventory_success() {
        // Mock the repository to return the user when searched by username
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        // Mock the repository to save the new inventory
        when(inventoryRepo.save(any(Inventory.class))).thenReturn(inventory);

        // Call the method to add a new inventory
        Inventory result = inventoryService.addInventory("New Inventory", "testUser");

        // Ensure the returned inventory has the expected values
        assertEquals("Test Inventory", result.getName());
    }

    @Test
    public void testAddInventory_userNotFound() {
        // Mock the repository to return an empty result when searching for a non-existent user
        when(userRepo.findByUsername("invalidUser")).thenReturn(Optional.empty());

        // Verify that an exception is thrown when trying to add an inventory for a non-existent user
        Exception exception = assertThrows(RuntimeException.class, () ->
                inventoryService.addInventory("New Inventory", "invalidUser"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateInventory_success() {
        // Mock the repository to update and return the modified inventory
        when(inventoryRepo.save(inventory)).thenReturn(inventory);

        // Call the method to update the inventory
        Inventory result = inventoryService.updateInventory(inventory);

        // Ensure the updated inventory has the expected values
        assertEquals("Test Inventory", result.getName());
    }

    @Test
    public void testDeleteInventoryById_success() {
        // Mock the repository to perform deletion without errors
        doNothing().when(inventoryRepo).deleteById(1L);

        // Call the method and verify no exceptions occur
        assertDoesNotThrow(() -> inventoryService.deleteInventory(1L));
        verify(inventoryRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteInventoryByObject_success() {
        // Mock the repository to perform deletion without errors
        doNothing().when(inventoryRepo).delete(inventory);

        // Call the method and verify no exceptions occur
        assertDoesNotThrow(() -> inventoryService.deleteInventory(inventory));
        verify(inventoryRepo, times(1)).delete(inventory);
    }

    @Test
    public void testGetInventoryNamesByUser_success() {
        // Mock the repository to return a user and their inventory list
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(inventoryRepo.findByUser(user)).thenReturn(List.of(inventory));

        // Call the method to get inventory names
        List<String> names = inventoryService.getAllInventoryNamesByUser("testUser");

        // Ensure the returned list contains the expected inventory name
        assertEquals(1, names.size());
        assertEquals("Test Inventory", names.get(0));
    }

    @Test
    public void testGetInventoryNamesByUser_userNotFound() {
        // Mock the repository to return an empty result when searching for a non-existent user
        when(userRepo.findByUsername("invalidUser")).thenReturn(Optional.empty());

        // Verify that an exception is thrown when trying to retrieve inventory names for a non-existent user
        Exception exception = assertThrows(RuntimeException.class, () ->
                inventoryService.getAllInventoryNamesByUser("invalidUser"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testFindById_success() {
        // Mock the repository to return an inventory when searched by a valid ID
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventory));

        // Call the method to fetch inventory by ID
        Optional<Inventory> result = inventoryService.findById(1L);

        // Ensure the inventory was found and matches the expected values
        assertTrue(result.isPresent());
        assertEquals("Test Inventory", result.get().getName());

        // Verify that the repository method was called exactly once
        verify(inventoryRepo, times(1)).findById(1L);
    }

    @Test
    public void testFindById_notFound() {
        // Mock the repository to return an empty result when searching by a non-existent ID
        when(inventoryRepo.findById(999L)).thenReturn(Optional.empty());

        // Call the method and verify that no inventory is found
        Optional<Inventory> result = inventoryService.findById(999L);
        assertFalse(result.isPresent());

        // Verify that the repository method was called exactly once
        verify(inventoryRepo, times(1)).findById(999L);
    }


}
