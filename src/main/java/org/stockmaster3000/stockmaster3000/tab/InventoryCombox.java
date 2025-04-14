package org.stockmaster3000.stockmaster3000.tab;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.ArrayList;
import java.util.List;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;

/**
 * A reusable component for selecting, adding, or deleting user inventories.
 */
public class InventoryCombox extends VerticalLayout {

  private final ComboBox<Inventory> inventoryComboBox;
  private final List<SelectionListener> listeners = new ArrayList<>();
  private final InventoryService inventoryService;
  private final SecurityService securityService;

  private Inventory selectedInventory;
  private ComboBox<Inventory> inventoryDialogComboBox;

  Button addInventoryButton;
  Button deleteInventoryButton;
  Button deleteButton;
  Button closeButton;
  TextField nameField;

  /**
   * Constructs the inventory selection and management combo box.
   *
   * @param securityService the security service to get the current user
   * @param inventoryService the inventory service for data access
   */
  public InventoryCombox(SecurityService securityService, InventoryService inventoryService) {
    this.securityService = securityService;
    this.inventoryService = inventoryService;

    inventoryComboBox = new ComboBox<>(getTranslation("inv.sel"));
    inventoryComboBox.setItemLabelGenerator(Inventory::getName);
    refreshInventories();

    inventoryComboBox.addValueChangeListener(event -> {
      selectedInventory = event.getValue();
      notifySelectionListeners(selectedInventory);
    });

    addInventoryButton = new Button(getTranslation("inv.add"),
        e -> showAddInventoryDialog());
    deleteInventoryButton = new Button(getTranslation("inv.del"),
        e -> showDeleteInventoryDialog());
    deleteInventoryButton.addClassName("delete-inventory-button");

    HorizontalLayout inventoryLayout = new HorizontalLayout(
        inventoryComboBox, addInventoryButton, deleteInventoryButton);
    inventoryLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
    inventoryLayout.setSpacing(true);

    add(inventoryLayout);
  }

  /**
   * Displays the dialog to delete an inventory selected by the user.
   */
  private void showDeleteInventoryDialog() {
    final Dialog dialog = new Dialog();
    inventoryDialogComboBox = new ComboBox<>(getTranslation("inv.sel"));
    inventoryDialogComboBox.setItemLabelGenerator(Inventory::getName);

    List<Inventory> inventories = inventoryService.getAllInventoriesByUser(getCurrentUsername());
    inventoryDialogComboBox.setItems(inventories);

    deleteButton = new Button(getTranslation("delete"), e -> {
      Inventory inventory = inventoryDialogComboBox.getValue();

      if (inventory == null) {
        Notification.show(getTranslation("inv.sel_noti"));
        return;
      }

      try {
        inventoryService.deleteInventory(inventory);

        inventoryDialogComboBox.setItems(
            inventoryService.getAllInventoriesByUser(getCurrentUsername()));
        inventoryComboBox.setItems(
            inventoryService.getAllInventoriesByUser(getCurrentUsername()));

        dialog.close();
        Notification.show(getTranslation("succ.inv_del"));
      } catch (Exception ex) {
        Notification.show(getTranslation("err.inv_del") + ex.getMessage());
      }
    });

    closeButton = new Button(getTranslation("close"), e -> dialog.close());
    closeButton.addClassName("close-button");

    HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, closeButton);
    buttonLayout.setSpacing(true);
    buttonLayout.addClassName("modal-button-layout");

    VerticalLayout contentLayout = new VerticalLayout(inventoryDialogComboBox, buttonLayout);
    contentLayout.setSpacing(true);

    dialog.add(contentLayout);
    dialog.open();
  }

  /**
   * Displays the dialog to add a new inventory.
   */
  private void showAddInventoryDialog() {
    Dialog dialog = new Dialog();
    nameField = new TextField(getTranslation("inv.name"));

    Button saveButton = new Button(getTranslation("save"), e -> {
      String inventoryName = nameField.getValue();

      if (inventoryName == null || inventoryName.trim().isEmpty()) {
        Notification.show(getTranslation("provide.inv_name"));
        return;
      }

      try {
        Inventory newInventory = inventoryService.addInventory(
            inventoryName, getCurrentUsername());

        inventoryComboBox.setItems(
            inventoryService.getAllInventoriesByUser(getCurrentUsername()));
        inventoryComboBox.setValue(newInventory);

        dialog.close();
        Notification.show(getTranslation("succ.inv_add"));
      } catch (Exception ex) {
        Notification.show(getTranslation("err.inv_add") + ex.getMessage());
      }
    });

    saveButton.addClassName("save-button");

    Button closeButton = new Button(getTranslation("close"), e -> dialog.close());
    closeButton.addClassName("close-button");

    HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, closeButton);
    buttonLayout.setSpacing(true);
    buttonLayout.setPadding(false);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

    VerticalLayout buttonWrapper = new VerticalLayout(nameField, buttonLayout);
    buttonWrapper.setSpacing(true);
    buttonWrapper.setPadding(false);
    buttonWrapper.setAlignItems(FlexComponent.Alignment.CENTER);

    dialog.add(buttonWrapper);
    dialog.open();
  }

  /**
   * Loads all inventories for the authenticated user.
   */
  private void refreshInventories() {
    List<Inventory> inventories =
        inventoryService.getAllInventoriesByUser(getCurrentUsername());
    inventoryComboBox.setItems(inventories);
  }

  /**
   * Registers a listener for inventory selection changes.
   *
   * @param listener the listener to be notified
   */
  public void setSelectionListener(SelectionListener listener) {
    listeners.add(listener);
  }

  private void notifySelectionListeners(Inventory selectedInventory) {
    for (SelectionListener listener : listeners) {
      listener.onInventorySelected(selectedInventory);
    }
  }

  /**
   * Returns the currently selected inventory.
   *
   * @return selected inventory
   */
  public Inventory getSelectedInventory() {
    return selectedInventory;
  }

  /**
   * Returns the ID of the currently selected inventory.
   *
   * @return inventory ID
   */
  public long getInventoryId() {
    return selectedInventory.getId();
  }

  /**
   * Listener interface to handle inventory selection events.
   */
  public interface SelectionListener {
    /**
     * Called when an inventory is selected.
     *
     * @param selectedInventory the selected inventory instance
     */
    void onInventorySelected(Inventory selectedInventory);
  }


  private String getCurrentUsername() {
    return securityService.getAuthenticatedUser().getUsername();
  }

  /**
   * Updates all translatable text based on the current locale.
   */
  public void updateTexts() {
    inventoryComboBox.setLabel(getTranslation("inv.sel"));
    addInventoryButton.setText(getTranslation("inv.add"));
    deleteInventoryButton.setText(getTranslation("inv.del"));
  }
}
