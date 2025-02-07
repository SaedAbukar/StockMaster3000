package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.security.PermitAll;
import org.stockmaster3000.stockmaster3000.model.*;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Route("inventory")
@PageTitle("Inventory Management")
@PermitAll
public class InventoryView extends VerticalLayout {

    private final SecurityService securityService;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    private Grid<Product> grid = new Grid<>(Product.class, false);
    private ComboBox<Inventory> inventoryComboBox;
    private String currentFilter = "ALL";

    public InventoryView(SecurityService securityService, InventoryService inventoryService, ProductService productService, CategoryService categoryService, SupplierService supplierService) {
        this.securityService = securityService;
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;

        addClassName("inventory-view");
        setSizeFull();
        createHeader();
        createInventorySelector();
        createFilterButtons();
        createGrid();
        createInventoryButton();
        createDeleteInventoryButton();
        updateGrid();
    }

    private void createHeader() {
        // Header container
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);

        header.getStyle().set("background-color", "#03fc7f")
                .set("padding", "5px")
                .set("color", "white");

        H1 title = new H1();
        title.addClassName("logo");
        title.getElement().setProperty("innerHTML", "StockMaster <span>3000</span>");
        title.getStyle().set("color", "white");

        HorizontalLayout authSection = new HorizontalLayout();
        authSection.setWidthFull();
        authSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        authSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Button login = new Button("Login");
        if (securityService.getAuthenticatedUser() != null) {
            String username = securityService.getAuthenticatedUser().getUsername();
            Span greeting = new Span("Hello, " + username + "!");
            greeting.getStyle().set("color", "white");
            Button logout = new Button("Logout", click -> securityService.logout());
            logout.getStyle().set("color", "white");
            authSection.add(greeting, logout);
        } else {
            login.addClickListener(click -> login.getUI().ifPresent(ui -> ui.navigate("login")));
            login.getStyle().set("color", "white");
            authSection.add(login);
        }

        header.add(title, authSection);
        add(header);
    }

    private void createInventorySelector() {
        inventoryComboBox = new ComboBox<>("Select Inventory");
        inventoryComboBox.setItemLabelGenerator(Inventory::getName);

        String username = getCurrentUsername();
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser(username);
        inventoryComboBox.setItems(inventories);

        inventoryComboBox.addValueChangeListener(event -> updateGrid());
        add(inventoryComboBox);
    }

    private void createFilterButtons() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        Button addButton = new Button("Add Product", e -> showAddProductDialog());
        Button allButton = createFilterButton("All", "ALL", true);
        Button expiringButton = createFilterButton("Expiring Soon", "EXPIRING", false);
        Button lowStockButton = createFilterButton("Low Stock", "LOW", false);
        Button outOfStockButton = createFilterButton("Out of Stock", "OUT", false);

        filterLayout.add(addButton, allButton, expiringButton, lowStockButton, outOfStockButton);
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
            updateGrid();
        });

        return button;
    }

    private void updateActiveButton(Button selectedButton) {
        getChildren().forEach(component -> {
            if (component instanceof HorizontalLayout) {
                ((HorizontalLayout) component).getChildren()
                        .filter(btn -> btn instanceof Button)
                        .forEach(btn -> btn.removeClassName("active"));
            }
        });
        selectedButton.addClassName("active");
    }

    private void createGrid() {
        grid.addColumn(Product::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Product::getQuantity).setHeader("Quantity").setSortable(true);
        grid.addColumn(Product::getPrice).setHeader("Price").setSortable(true);
        grid.addColumn(Product::getAmountOfDaysUntilExpiration).setHeader("Days Until Expiration").setSortable(true);
        grid.addColumn(product -> product.getCategory().getName()).setHeader("Category").setSortable(true);
        grid.addColumn(product -> product.getSupplier().getName()).setHeader("Supplier").setSortable(true);

        // Add buttons for editing and deleting products
        grid.addComponentColumn(product -> {
            HorizontalLayout buttons = new HorizontalLayout();
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> showEditProductDialog(product));
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> deleteProduct(product));
            buttons.add(editButton, deleteButton);
            return buttons;
        });

        add(grid);
    }

    private void createInventoryButton() {
        Button addInventoryButton = new Button("Add Inventory", e -> showAddInventoryDialog());
        add(addInventoryButton);
    }

    private void createDeleteInventoryButton() {
        Button deleteInventoryButton = new Button("Delete Inventory", e -> showDeleteInventoryDialog());
        add(deleteInventoryButton);
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
        Inventory selectedInventory = inventoryComboBox.getValue();
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
                updateGrid();
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
                updateGrid();
                dialog.close();
                Notification.show("Product updated successfully");
            } catch (NumberFormatException ex) {
                Notification.show("Invalid quantity or price");
            }
        });

        dialog.add(nameField, quantityField, priceField, expirationDate, supplierField, categoryField, saveButton);
        dialog.open();
    }


    private void deleteProduct(Product product) {
        productService.deleteProduct(product.getId());
        updateGrid();
        Notification.show("Product deleted successfully");
    }
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


    private void updateGrid() {
        Inventory selectedInventory = inventoryComboBox.getValue();
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

    private String getCurrentUsername() {
        return securityService.getAuthenticatedUser().getUsername();
    }
}
