package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;

import java.util.ArrayList;
import java.util.List;


public class InventorySelectorComponent extends VerticalLayout {

    private final ComboBox<Inventory> inventoryComboBox;
    private final List<SelectionListener> listeners = new ArrayList<>();
    private final InventoryService inventoryService;
    private final SecurityService securityService;
    private Inventory selectedInventory; // Store the selected inventory

    // Component Constructor
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
    public InventorySelectorComponent(SecurityService securityService, InventoryService inventoryService) {
        this.securityService = securityService;
        this.inventoryService = inventoryService;

        // Initialize the combo box
        inventoryComboBox = new ComboBox<>("Select Inventory");
        inventoryComboBox.setItemLabelGenerator(Inventory::getName);

        // Load inventories for the current user
        refreshInventories();

        // Set up value change listener
        inventoryComboBox.addValueChangeListener(event -> {
            selectedInventory = event.getValue(); // Update the selected inventory
            notifySelectionListeners(selectedInventory); // Notify listeners of the change
        });

        // Create buttons for adding and deleting inventory
        Button addInventoryButton = new Button("Add Inventory", e -> showAddInventoryDialog());
        Button deleteInventoryButton = new Button("Delete Inventory", e -> showDeleteInventoryDialog());
        deleteInventoryButton.addClassName("delete-inventory-button"); // Apply red style

        // Create a layout to align all elements horizontally
        HorizontalLayout inventoryLayout = new HorizontalLayout(inventoryComboBox, addInventoryButton, deleteInventoryButton);
        inventoryLayout.setAlignItems(FlexComponent.Alignment.BASELINE); // Align elements properly
        inventoryLayout.setSpacing(true); // Add spacing between elements

        // Add the layout to the component
        add(inventoryLayout);
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------

    // Displays the Delete modal for the User to choose which Inventory to Delete
    private void showDeleteInventoryDialog() {
        Dialog dialog = new Dialog();
        ComboBox<Inventory> inventoryDialogComboBox = new ComboBox<>("Select Inventory");
        inventoryDialogComboBox.setItemLabelGenerator(Inventory::getName);

        String username = getCurrentUsername();
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser(username);
        inventoryDialogComboBox.setItems(inventories);

        Button deleteButton = new Button("Delete", e -> {
            Inventory inventory = inventoryDialogComboBox.getValue();

            // Validation: Ensure the name is not empty
            if (inventory == null) {
                Notification.show("Please select an inventory.");
                return;
            }

            try {
                // Pass the inventory name and the current user to the InventoryService
                inventoryService.deleteInventory(inventory);

                // Update the ComboBox with the new list of inventories
                inventoryDialogComboBox.setItems(inventoryService.getAllInventoriesByUser(getCurrentUsername()));
                // Update the ComboBox with the new list of inventories
                inventoryComboBox.setItems(inventoryService.getAllInventoriesByUser(getCurrentUsername()));

                dialog.close();
                Notification.show("Inventory deleted successfully");
            } catch (Exception ex) {
                // Improved error handling with the exception message
                Notification.show("Error while deleting inventory: " + ex.getMessage());
            }
        });
        // Create the Close button
        Button closeButton = new Button("Close", e -> dialog.close());

        dialog.add(inventoryDialogComboBox, deleteButton, closeButton);
        dialog.open();
    }

    // Displays the Add Inventory modal, for User to add new Inventories
    private void showAddInventoryDialog() {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Inventory Name");

        Button saveButton = new Button("Save", e -> {
            String inventoryName = nameField.getValue();

            // Validation: Ensure the name is not empty
            if (inventoryName == null || inventoryName.trim().isEmpty()) {
                Notification.show("Please provide a valid inventory name.");
                return;
            }

            try {
                // Pass the inventory name and the current user to the InventoryService
                Inventory newInventory = inventoryService.addInventory(inventoryName, securityService.getAuthenticatedUser().getUsername());

                // Update the ComboBox with the new list of inventories
                inventoryComboBox.setItems(inventoryService.getAllInventoriesByUser(getCurrentUsername()));
                inventoryComboBox.setValue(newInventory); // Select the newly created inventory

                dialog.close();
                Notification.show("Inventory added successfully");
            } catch (Exception ex) {
                // Improved error handling with the exception message
                Notification.show("Error while creating inventory: " + ex.getMessage());
            }
        });
        // Create the Close button
        Button closeButton = new Button("Close", e -> dialog.close());

        dialog.add(nameField, saveButton, closeButton);
        dialog.open();
    }

    // Loads all of the Inventories under the User's username
    private void refreshInventories() {
        String username = securityService.getAuthenticatedUser().getUsername();
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser(username);
        inventoryComboBox.setItems(inventories);
    }


    public void setSelectionListener(SelectionListener listener) {
        listeners.add(listener);
    }

    private void notifySelectionListeners(Inventory selectedInventory) {
        for (SelectionListener listener : listeners) {
            listener.onInventorySelected(selectedInventory);
        }
    }

    public Inventory getSelectedInventory() {
        return selectedInventory; // Return the currently selected inventory
    }

    public long getInventoryId() {
        return selectedInventory.getId();
    }

    public interface SelectionListener {
        void onInventorySelected(Inventory selectedInventory);
    }

    private String getCurrentUsername() {
        return securityService.getAuthenticatedUser().getUsername();
    }
}
