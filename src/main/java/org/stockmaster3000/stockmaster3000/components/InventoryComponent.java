package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.security.PermitAll;

import org.stockmaster3000.stockmaster3000.model.*;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@PermitAll
public class InventoryComponent extends VerticalLayout {

    private final SecurityService securityService;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final InventorySelectorComponent inventorySelectorComponent;

    private HorizontalLayout filterLayout;

    private Grid<Product> grid = new Grid<>(Product.class, false);
    private ComboBox<Inventory> inventoryComboBox;
    private String currentFilter = "ALL";

    public InventoryComponent(SecurityService securityService, InventoryService inventoryService, ProductService productService, CategoryService categoryService, SupplierService supplierService) {
        this.securityService = securityService;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;

        this.inventorySelectorComponent = new InventorySelectorComponent(securityService, inventoryService);

        addClassName("inventory-view");
        searchByName();
        createFilterButtons();
        createGrid();
        updateGrid(inventorySelectorComponent.getSelectedInventory());
    }


    private void createFilterButtons() {
        // Layout for filter buttons
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
    
        Button allButton = createFilterButton("All", "ALL", true);
        Button expiringButton = createFilterButton("Expiring Soon", "EXPIRING", false);
        Button lowStockButton = createFilterButton("Low Stock", "LOW", false);
        Button outOfStockButton = createFilterButton("Out of Stock", "OUT", false);
    
        // Apply neutral color class
        allButton.addClassName("neutral-button");
        expiringButton.addClassName("neutral-button");
        lowStockButton.addClassName("neutral-button");
        outOfStockButton.addClassName("neutral-button");
    
        filterButtons.add(allButton, expiringButton, lowStockButton, outOfStockButton);
    
        // "+ Add Product" button
        Button addButton = new Button("+ Add Product", e -> showAddProductDialog());
        addButton.addClassName("add-button");
    
        // Main layout to position elements
        filterLayout = new HorizontalLayout(filterButtons, addButton);
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Filter buttons left, Add button right
        filterLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout.addClassName("filter-layout");
    
        add(filterLayout);
    }
    
    private Button createFilterButton(String text, String filter, boolean isActive) {
        Button button = new Button(text);
        button.addClassName("filter-button");
    
        if (isActive) {
            button.addClassName("active");
            currentFilter = filter;
        }
    
        button.addClickListener(e -> {
            updateActiveButton(button);
            currentFilter = filter;
            updateGrid(inventorySelectorComponent.getSelectedInventory());
        });
    
        return button;
    }
    
    private void updateActiveButton(Button selectedButton) {
        if (filterLayout != null) {
            // Get the filter buttons layout (first child of filterLayout)
            filterLayout.getComponentAt(0)
                    .getChildren()
                    .filter(component -> component instanceof Button)
                    .map(component -> (Button) component)
                    .forEach(button -> {
                        button.removeClassName("active");
                        button.addClassName("neutral-button");
                    });
        }

        // Mark the selected button as active
        selectedButton.addClassName("active");
        selectedButton.removeClassName("neutral-button");
    }
    
    private void createGrid() {
        grid.addClassName("inventory-grid");
        grid.setSelectionMode(Grid.SelectionMode.NONE); // Disable row selection
    
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Product::getQuantity).setHeader("Quantity").setSortable(true);
        grid.addColumn(Product::getPrice).setHeader("Price").setSortable(true);
        grid.addColumn(Product::getAmountOfDaysUntilExpiration).setHeader("Days Until Expiration").setSortable(true);
        grid.addColumn(product -> product.getCategory().getName()).setHeader("Category").setSortable(true);
        grid.addColumn(product -> product.getSupplier().getName()).setHeader("Supplier").setSortable(true);
    
        // Add a class name to each row for styling
        grid.setPartNameGenerator(item -> "clickable-row");
    
        // Apply hover effect dynamically
        grid.getElement().executeJs(
            "this.shadowRoot.querySelectorAll('tr').forEach(row => { " +
            "    row.style.cursor = 'pointer'; " +
            "    row.addEventListener('mouseover', () => row.style.backgroundColor = 'rgba(0, 150, 136, 0.1)'); " +
            "    row.addEventListener('mouseout', () => row.style.backgroundColor = ''); " +
            "});"
        );
    
        // Make the entire row clickable
        grid.addItemClickListener(event -> showProductActionsDialog(event.getItem()));
    
        add(grid);
    }
    
    private void showProductActionsDialog(Product product) {
        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
    
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
    
        H2 title = new H2("Product Actions");
        layout.add(title);
    
        // Display product details
        layout.add(new Span("Name: " + product.getName()));
        layout.add(new Span("Quantity: " + product.getQuantity()));
        layout.add(new Span("Price: " + product.getPrice()));
        layout.add(new Span("Days Until Expiration: " + product.getAmountOfDaysUntilExpiration()));
        layout.add(new Span("Category: " + product.getCategory().getName()));
        layout.add(new Span("Supplier: " + product.getSupplier().getName()));
    
        Button editButton = new Button("Edit", e -> {
            dialog.close();
            showEditProductDialog(product);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    
        Button deleteButton = new Button("Delete", e -> {
            dialog.close();
            deleteProduct(product);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    
        HorizontalLayout buttonLayout = new HorizontalLayout(editButton, deleteButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    
        layout.add(buttonLayout);
    
        dialog.add(layout);
        dialog.open();
    }    

    private void showAddProductDialog() {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Product Name");
        TextField quantityField = new TextField("Quantity");
        TextField priceField = new TextField("Price");
        DatePicker expirationDate = new DatePicker("Expiration Date");

        // TextFields for Supplier and Category
        TextField supplierField = new TextField("Supplier");
        TextField categoryField = new TextField("Category");

        // Get selected inventory
        Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
        if (selectedInventory == null) {
            Notification.show("Please select an inventory first");
            return;
        }

        Button saveButton = new Button("Save", e -> {
            try {
                Product newProduct = new Product();
                newProduct.setName(nameField.getValue());
                newProduct.setQuantity(Integer.parseInt(quantityField.getValue()));
                newProduct.setPrice(Double.parseDouble(priceField.getValue()));

                if (expirationDate.getValue() != null) {
                    long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate.getValue());
                    newProduct.setAmountOfDaysUntilExpiration((int) daysUntilExpiration);
                } else {
                    Notification.show("Please select a valid expiration date.");
                }


                Inventory inventory = inventoryService.findById(selectedInventory.getId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));
                newProduct.setInventory(inventory);


                // Set Supplier and Category from TextFields
                String supplierName = supplierField.getValue().trim();
                String categoryName = categoryField.getValue().trim();

                if (supplierName.isEmpty() || categoryName.isEmpty()) {
                    Notification.show("Supplier and Category cannot be empty");
                    return;
                }
                Supplier supplier = supplierService.findByName(supplierName)
                        .orElseGet(() -> {
                            Supplier newSupplier = new Supplier(supplierName);
                            return supplierService.save(newSupplier);
                        });

                Category category = categoryService.findByName(categoryName)
                        .orElseGet(() -> {
                            Category newCategory = new Category(categoryName);
                            return categoryService.save(newCategory);
                        });

                newProduct.setSupplier(supplier);
                newProduct.setCategory(category);


                productService.addProduct(newProduct);
                updateGrid(inventorySelectorComponent.getSelectedInventory());
                dialog.close();
                Notification.show("Product added successfully");
            } catch (NumberFormatException ex) {
                Notification.show("Invalid quantity or price");
            }
        });

        dialog.add(nameField, quantityField, priceField, expirationDate, supplierField, categoryField, saveButton);
        dialog.open();
    }

    private void showEditProductDialog(Product product) {
        Dialog dialog = new Dialog();
        TextField nameField = new TextField("Product Name");
        nameField.setValue(product.getName());

        TextField quantityField = new TextField("Quantity");
        quantityField.setValue(String.valueOf(product.getQuantity()));

        TextField priceField = new TextField("Price");
        priceField.setValue(String.valueOf(product.getPrice()));

        DatePicker expirationDate = new DatePicker("Expiration Date");

        TextField supplierField = new TextField("Supplier");
        supplierField.setValue(product.getSupplier().getName());

        TextField categoryField = new TextField("Category");
        categoryField.setValue(product.getCategory().getName());


        Button saveButton = new Button("Save", e -> {
            try {
                product.setName(nameField.getValue());
                product.setQuantity(Integer.parseInt(quantityField.getValue()));
                product.setPrice(Double.parseDouble(priceField.getValue()));
                if (expirationDate.getValue() != null) {
                    long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate.getValue());
                    product.setAmountOfDaysUntilExpiration((int) daysUntilExpiration);
                } else {
                    Notification.show("Please select a valid expiration date.");
                }

                // Ensure an inventory is selected
                Inventory selectedInventory = product.getInventory();
                if (selectedInventory == null) {
                    Notification.show("Please select an inventory");
                    return;
                }
                product.setInventory(selectedInventory);

                // Fetch or Create Supplier & Category
                String supplierName = supplierField.getValue().trim();
                String categoryName = categoryField.getValue().trim();

                if (nameField.getValue().isEmpty()) {
                    Notification.show("Supplier name cannot be empty");
                    return;
                }

                if (supplierName.isEmpty() || categoryName.isEmpty()) {
                    Notification.show("Supplier and Category cannot be empty");
                    return;
                }

                Supplier supplier = supplierService.findByName(supplierName)
                        .orElseGet(() -> supplierService.save(new Supplier(supplierName)));

                Category category = categoryService.findByName(categoryName)
                        .orElseGet(() -> categoryService.save(new Category(categoryName)));

                product.setSupplier(supplier);
                product.setCategory(category);

                // Update the product in the database
                productService.updateProduct(product);
                updateGrid(inventorySelectorComponent.getSelectedInventory());
                dialog.close();
                Notification.show("Product updated successfully");
            } catch (NumberFormatException ex) {
                Notification.show("Invalid quantity or price");
            }
        });

        dialog.add(nameField, quantityField, priceField, expirationDate, supplierField, categoryField, saveButton);
        dialog.open();
    }

    // Deleting the Product from inventory and updates the grid
    private void deleteProduct(Product product) {
        productService.deleteProduct(product.getId());
        updateGrid(inventorySelectorComponent.getSelectedInventory());
        Notification.show("Product deleted successfully");
    }

    private void searchByName() {
        TextField searchbar = new TextField();
        searchbar.setPlaceholder("Search Product");
    
        Button searchButton = new Button("Search");
    
        searchButton.addClickListener(event -> {
            Inventory inventory = inventorySelectorComponent.getSelectedInventory();
    
            // Validation: Ensure an inventory is selected
            if (inventory == null) {
                Notification.show("Please select an inventory.");
                return;
            }
    
            String searchText = searchbar.getValue().trim();
            if (!searchText.isEmpty()) {
                List<Product> products = productService.getProductsByName(inventory.getId(), searchText);
                grid.setItems(products);
            } else {
                List<Product> products = productService.getProductsByInventory(inventory.getId());
                grid.setItems(products);
            }
        });
    
        // Add CSS classes
        searchbar.addClassName("searchbar");
        searchButton.addClassName("search-button");
    
        // **Align search bar under the inventory selection**
        HorizontalLayout searchLayout = new HorizontalLayout(searchbar, searchButton);
        searchLayout.addClassName("search-layout");
        searchLayout.setWidthFull();
    
        // ✅ Now add it **after** the inventory selection layout
        add(searchLayout);
    }

    public void updateGrid(Inventory selectedInventory) {
        if (selectedInventory != null) {
            List<Product> products = getFilteredProducts(currentFilter, selectedInventory);
            grid.setItems(products);
        } else {
            grid.setItems();
        }
    }

    private List<Product> getFilteredProducts(String filter, Inventory inventory) {
        List<Product> filteredProducts = productService.getProductsByInventory(inventory.getId());

        switch (filter) {
            case "EXPIRING":
                filteredProducts = productService.getExpiringSoonItems(inventory.getId());
                break;
            case "LOW":
                filteredProducts = productService.getLowStockItems(inventory.getId());
                break;
            case "OUT":
                filteredProducts = productService.getOutOfStockItems(inventory.getId());
                break;
            case "ALL":
            default:
                break;
        }

        return filteredProducts;
    }
}
