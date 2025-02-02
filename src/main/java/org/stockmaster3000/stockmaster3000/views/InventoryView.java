package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryView extends VerticalLayout {
    private Grid<InventoryItem> grid;
    private String currentFilter = "ALL";
    private List<InventoryItem> items;
    
    public InventoryView() {
        addClassName("inventory-view");
        setSizeFull();
        
        items = new ArrayList<>();
        
        createTitle();
        createFilterButtons();
        createGrid();
        loadSampleData();
    }
    
    private void createTitle() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        
        H2 title = new H2("Inventory");
        Button addButton = new Button("Add Item", VaadinIcon.PLUS.create());
        addButton.addClassName("add-button"); // Add custom class for green styling
        addButton.addClickListener(e -> showAddItemDialog());        
        
        header.add(title, addButton);
        add(header);
    }
    
    private void createFilterButtons() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
    
        Button allButton = createFilterButton("All", "ALL");
        allButton.addClassName("filter-button"); // Custom styling
        Button expiringButton = createFilterButton("Expires Soon", "EXPIRING");
        expiringButton.addClassName("filter-button"); // Custom styling
        Button lowStockButton = createFilterButton("Low Stock", "LOW");
        lowStockButton.addClassName("filter-button"); // Custom styling
        Button outOfStockButton = createFilterButton("Out of Stock", "OUT");
        outOfStockButton.addClassName("filter-button"); // Custom styling
    
        filterLayout.add(allButton, expiringButton, lowStockButton, outOfStockButton);
        add(filterLayout);
    }
    
    
    private Button createFilterButton(String text, String filter) {
        Button button = new Button(text);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        if (filter.equals(currentFilter)) {
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        button.addClickListener(e -> {
            currentFilter = filter;
            updateGrid();
        });
        return button;
    }
    
    private void createGrid() {
        grid = new Grid<>(InventoryItem.class, false);
        grid.addClassName("inventory-grid");
        
        grid.addColumn(InventoryItem::getName)
            .setHeader("Name")
            .setSortable(true);
            
        grid.addColumn(InventoryItem::getType)
            .setHeader("Type")
            .setSortable(true);
            
        grid.addColumn(InventoryItem::getQuantity)
            .setHeader("Quantity")
            .setSortable(true);
            
        grid.addColumn(InventoryItem::getExpirationDate)
            .setHeader("Expires")
            .setSortable(true);
            
        // Add edit and delete buttons
        grid.addComponentColumn(item -> {
            HorizontalLayout buttons = new HorizontalLayout();
            
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClassName("edit-button"); // Add custom class for orange styling
            editButton.addClickListener(e -> showEditItemDialog(item));
            
            
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> deleteItem(item));
            
            buttons.add(editButton, deleteButton);
            return buttons;
        });
        
        add(grid);
    }
    
    private void showAddItemDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add New Item");
        
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField nameField = new TextField("Name");
        TextField typeField = new TextField("Type");
        TextField quantityField = new TextField("Quantity");
        DatePicker expirationDate = new DatePicker("Expiration Date");
        
        Button saveButton = new Button("Save", e -> {
            try {
                InventoryItem newItem = new InventoryItem(
                    nameField.getValue(),
                    typeField.getValue(),
                    Integer.parseInt(quantityField.getValue()),
                    expirationDate.getValue()
                );
                items.add(newItem);
                updateGrid();
                dialog.close();
                Notification.show("Item added successfully");
            } catch (NumberFormatException ex) {
                Notification.show("Please enter a valid quantity");
            }
        });
        
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        
        dialogLayout.add(nameField, typeField, quantityField, expirationDate, buttons);
        dialog.add(dialogLayout);
        dialog.open();
    }
    
    private void showEditItemDialog(InventoryItem item) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Item");
        
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField nameField = new TextField("Name", item.getName());
        TextField typeField = new TextField("Type", item.getType());
        TextField quantityField = new TextField("Quantity", String.valueOf(item.getQuantity()));
        DatePicker expirationDate = new DatePicker("Expiration Date", item.getExpirationDate());
        
        Button saveButton = new Button("Save", e -> {
            try {
                item.setName(nameField.getValue());
                item.setType(typeField.getValue());
                item.setQuantity(Integer.parseInt(quantityField.getValue()));
                item.setExpirationDate(expirationDate.getValue());
                updateGrid();
                dialog.close();
                Notification.show("Item updated successfully");
            } catch (NumberFormatException ex) {
                Notification.show("Please enter a valid quantity");
            }
        });
        
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        
        dialogLayout.add(nameField, typeField, quantityField, expirationDate, buttons);
        dialog.add(dialogLayout);
        dialog.open();
    }
    
    private void deleteItem(InventoryItem item) {
        items.remove(item);
        updateGrid();
        Notification.show("Item deleted successfully");
    }
    
    private void loadSampleData() {
        items.add(new InventoryItem("Milk", "Dairy", 5, LocalDate.now().plusDays(7)));
        items.add(new InventoryItem("Bread", "Bakery", 2, LocalDate.now().plusDays(3)));
        items.add(new InventoryItem("Cheese", "Dairy", 0, LocalDate.now().plusDays(14)));
        items.add(new InventoryItem("Apples", "Produce", 15, LocalDate.now().plusDays(10)));
        items.add(new InventoryItem("Chicken", "Meat", 3, LocalDate.now().plusDays(2)));
        
        updateGrid();
    }
    
    private void updateGrid() {
        grid.setItems(getFilteredItems(currentFilter));
    }
    
    private List<InventoryItem> getFilteredItems(String filter) {
        List<InventoryItem> filteredItems = new ArrayList<>();
        
        for (InventoryItem item : items) {
            switch (filter) {
                case "EXPIRING":
                    if (item.getExpirationDate().isBefore(LocalDate.now().plusDays(7))) {
                        filteredItems.add(item);
                    }
                    break;
                case "LOW":
                    if (item.getQuantity() < 5) {
                        filteredItems.add(item);
                    }
                    break;
                case "OUT":
                    if (item.getQuantity() == 0) {
                        filteredItems.add(item);
                    }
                    break;
                case "ALL":
                default:
                    filteredItems.add(item);
                    break;
            }
        }
        
        return filteredItems;
    }
}