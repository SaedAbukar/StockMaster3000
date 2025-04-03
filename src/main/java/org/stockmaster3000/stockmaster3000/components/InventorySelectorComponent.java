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
    private ComboBox<Inventory> inventoryDialogComboBox;
    Button addInventoryButton;
    Button deleteInventoryButton;
    Button deleteButton;
    Button closeButton;
    TextField nameField;

    // Component Constructor
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
    public InventorySelectorComponent(SecurityService securityService, InventoryService inventoryService) {
        this.securityService = securityService;
        this.inventoryService = inventoryService;

        // Initialize the combo box
        inventoryComboBox = new ComboBox<>(getTranslation("inv.sel"));
        inventoryComboBox.setItemLabelGenerator(Inventory::getName);

        // Load inventories for the current user
        refreshInventories();

        // Set up value change listener
        inventoryComboBox.addValueChangeListener(event -> {
            selectedInventory = event.getValue(); // Update the selected inventory
            notifySelectionListeners(selectedInventory); // Notify listeners of the change
        });

        // Create buttons for adding and deleting inventory
        addInventoryButton = new Button(getTranslation("inv.add"), e -> showAddInventoryDialog());
        deleteInventoryButton = new Button(getTranslation("inv.del"), e -> showDeleteInventoryDialog());
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
        inventoryDialogComboBox = new ComboBox<>(getTranslation("inv.sel"));
        inventoryDialogComboBox.setItemLabelGenerator(Inventory::getName);

        String username = getCurrentUsername();
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser(username);
        inventoryDialogComboBox.setItems(inventories);

        deleteButton = new Button(getTranslation("delete"), e -> {
            Inventory inventory = inventoryDialogComboBox.getValue();

            // Validation: Ensure the name is not empty
            if (inventory == null) {
                Notification.show(getTranslation("inv.sel_noti"));
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
                Notification.show(getTranslation("succ.inv_del"));
            } catch (Exception ex) {
                // Improved error handling with the exception message
                Notification.show(getTranslation("err.inv_del") + ex.getMessage());
            }
        });
        // Create the Close button
        closeButton = new Button(getTranslation("close"), e -> dialog.close());
        closeButton.addClassName("close-button");

        // Create a HorizontalLayout for buttons with spacing
        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, closeButton);
        buttonLayout.setSpacing(true); // Enable spacing between buttons
        buttonLayout.addClassName("modal-button-layout"); // Custom CSS for fine-tuning

        // Wrap everything in a VerticalLayout
        VerticalLayout contentLayout = new VerticalLayout(inventoryDialogComboBox, buttonLayout);
        contentLayout.setSpacing(true); // Add spacing between dropdown and buttons

        dialog.add(contentLayout);
        dialog.open();
    }

    // Displays the Add Inventory modal, for User to add new Inventories
    private void showAddInventoryDialog() {
        Dialog dialog = new Dialog();
        nameField = new TextField(getTranslation("inv.name"));

        Button saveButton = new Button(getTranslation("save"), e -> {
            String inventoryName = nameField.getValue();

            // Validation: Ensure the name is not empty
            if (inventoryName == null || inventoryName.trim().isEmpty()) {
                Notification.show(getTranslation("provide.inv_name"));
                return;
            }

            try {
                // Pass the inventory name and the current user to the InventoryService
                Inventory newInventory = inventoryService.addInventory(inventoryName, securityService.getAuthenticatedUser().getUsername());

                // Update the ComboBox with the new list of inventories
                inventoryComboBox.setItems(inventoryService.getAllInventoriesByUser(getCurrentUsername()));
                inventoryComboBox.setValue(newInventory); // Select the newly created inventory

                dialog.close();
                Notification.show(getTranslation("succ.inv_add"));
            } catch (Exception ex) {
                // Improved error handling with the exception message
                Notification.show(getTranslation("err.inv_add") + ex.getMessage());
            }
        });
        // add CSS to button
        saveButton.addClassName("save-button");

        // Create the Close button
        Button closeButton = new Button(getTranslation("close"), e -> dialog.close());

        // add CSS to button
        closeButton.addClassName("close-button");

        // Create button layout to place them side by side
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, closeButton);
        buttonLayout.setSpacing(true);  // Enable default spacing between buttons
        buttonLayout.setPadding(false);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Wrap everything in a VerticalLayout
        VerticalLayout buttonWrapper = new VerticalLayout(nameField, buttonLayout);
        buttonWrapper.setSpacing(true);  // Add spacing between input and button section
        buttonWrapper.setPadding(false);
        buttonWrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(buttonWrapper);

   
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

    public void updateTexts(){
        this.inventoryComboBox.setLabel(getTranslation("inv.sel"));
        //this.inventoryDialogComboBox.setLabel(getTranslation("inv.sel"));
        this.addInventoryButton.setText(getTranslation("inv.add"));
        this.deleteInventoryButton.setText(getTranslation("inv.del"));
        //this.deleteButton.setText(getTranslation("inv.del"));
        //this.closeButton.setText(getTranslation("close"));
        //this.nameField.setLabel(getTranslation("inv.name"));
    }
}
