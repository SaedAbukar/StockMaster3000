package org.stockmaster3000.stockmaster3000.components;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.security.PermitAll;

import org.aspectj.weaver.SignatureUtils;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;
import org.stockmaster3000.stockmaster3000.model.*;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.*;

import com.vaadin.flow.component.UI;
import org.springframework.stereotype.Component;
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
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final InventorySelectorComponent inventorySelectorComponent;

    String currentLanguageCode = UI.getCurrent().getLocale().getLanguage();

    private HorizontalLayout filterLayout;

    private SecurityService securityService;
    private Grid<Product> grid = new Grid<>(Product.class, false);
    private ComboBox<Inventory> inventoryComboBox;
    private String currentFilter = "ALL";
    private Inventory currentInventory;
    Button button;
    Button allButton;
    Button expiringButton;
    Button lowStockButton;
    Button outOfStockButton;
    Button addButton;
    Button editButton;
    Button deleteButton;
    Button saveButton;
    H2 title;
    Span name;
    Span quantity;
    Span price;
    Span nutritions;
    Span days_until_exp;
    Span category;
    Span supplier;
    TextField nameField;
    TextField quantityField;
    TextField priceField;
    DatePicker expirationDate;
    TextField supplierField;
    TextField categoryField;
    TextField searchbar;
    Button searchButton;
    String getLanguage;




    OpenAIClient aiClient = new OpenAIClient();

    // Component Constructor
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
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
        updateGrid(currentInventory, currentLanguageCode);
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------

    // Creates the Layout for the buttons
    private void createFilterButtons() {
        // Layout for filter buttons
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
    
        allButton = createFilterButton(getTranslation("inventory.filter_all"), "ALL", true);
        expiringButton = createFilterButton(getTranslation("inventory.filter_exp"), "EXPIRING", false);
        lowStockButton = createFilterButton(getTranslation("inventory.filter_low"), "LOW", false);
        outOfStockButton = createFilterButton(getTranslation("inventory.filter_out"), "OUT", false);
    
        // Apply neutral color class
        allButton.addClassName("neutral-button");
        expiringButton.addClassName("neutral-button");
        lowStockButton.addClassName("neutral-button");
        outOfStockButton.addClassName("neutral-button");
    
        filterButtons.add(allButton, expiringButton, lowStockButton, outOfStockButton);
    
        // "+ Add Product" button
        addButton = new Button(getTranslation("inventory.add_pro"), e -> showAddProductDialog());
        addButton.addClassName("add-button");
    
        // Main layout to position elements
        filterLayout = new HorizontalLayout(filterButtons, addButton);
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Filter buttons left, Add button right
        filterLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        filterLayout.addClassName("filter-layout");
    
        add(filterLayout);
    }
    
    // Creating individual filter buttons
    private Button createFilterButton(String text, String filter, boolean isActive) {
        button = new Button(text);
        button.addClassName("filter-button");
    
        if (isActive) {
            button.addClassName("active");
            currentFilter = filter;
        }
    
        button.addClickListener(e -> {
            updateActiveButton(button);
            currentFilter = filter;
            updateGrid(currentInventory, currentLanguageCode);
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
    
    // Creating the inventory GRID
    private void createGrid() {
        grid.addClassName("inventory-grid");
        grid.setSelectionMode(Grid.SelectionMode.NONE); // Disable row selection

        grid.addColumn(Product::getName).setKey("name").setHeader(getTranslation("name")).setSortable(true);
        grid.addColumn(Product::getQuantity).setKey("quantity").setHeader(getTranslation("quantity")).setSortable(true);
        grid.addColumn(Product::getPrice).setKey("price").setHeader(getTranslation("price")).setSortable(true);
        grid.addColumn(Product::getNutritions).setKey("nutritions").setHeader(getTranslation("nutritions")).setSortable(true);
        grid.addColumn(Product::getAmountOfDaysUntilExpiration).setKey("days_until_expiration").setHeader(getTranslation("days_until_expiration")).setSortable(true);
        grid.addColumn(product -> product.getCategory().getName()).setKey("category").setHeader(getTranslation("category")).setSortable(true);
        grid.addColumn(product -> product.getSupplier().getName()).setKey("supplier").setHeader(getTranslation("supplier")).setSortable(true);


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
    
    // Displaying the modal when the customer clicks on the specific Product row
    private void showProductActionsDialog(Product product) {
        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
    
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
    
        title = new H2(getTranslation("inventory.product_actions"));
        name = new Span(getTranslation("name"));
        quantity = new Span(getTranslation("quantity"));
        price = new Span(getTranslation("price"));
        nutritions = new Span(getTranslation("nutritions"));
        days_until_exp = new Span(getTranslation("days_until_expiration"));
        category = new Span(getTranslation("category"));
        supplier = new Span(getTranslation("supplier"));
        layout.add(title);
    
        // Display product details
        layout.add(name.getText() + ": " + product.getName() + "\n");
        layout.add(quantity.getText() + ": " + product.getQuantity() + "\n");
        layout.add(price.getText() + ": " + product.getPrice() + "\n");
        layout.add(nutritions.getText() + ": " + product.getNutritions() + "\n");
        layout.add(days_until_exp.getText() + ": " + product.getAmountOfDaysUntilExpiration() + "\n");
        layout.add(category.getText() + ": " + product.getCategory().getName() + "\n");
        layout.add(supplier.getText() + ": " + product.getSupplier().getName() + "\n");
    
        editButton = new Button(getTranslation("edit"), e -> {
            dialog.close();
            showEditProductDialog(product);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    
        deleteButton = new Button(getTranslation("delete"), e -> {
            dialog.close();
            deleteProduct(product);
        });
        deleteButton.addClassName("close-button");
    
        HorizontalLayout buttonLayout = new HorizontalLayout(editButton, deleteButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    
        layout.add(buttonLayout);
    
        dialog.add(layout);
        dialog.open();
    }    

    // Displaying the Add Product Modal 
    private void showAddProductDialog() {
        Dialog dialog = new Dialog();
        nameField = new TextField(getTranslation("name"));
        quantityField = new TextField(getTranslation("quantity"));
        priceField = new TextField(getTranslation("price"));
        expirationDate = new DatePicker(getTranslation("exp_date"));
        supplierField = new TextField(getTranslation("supplier"));
        categoryField = new TextField(getTranslation("category"));
        String languageCode = UI.getCurrent().getLocale().getLanguage();

        // Get selected inventory
        Inventory selectedInventory = currentInventory;
        if (selectedInventory == null) {
            Notification.show(getTranslation("inv.sel_noti"));
            return;
        }

        saveButton = new Button(getTranslation("save"), e -> {
            try {
                // Fetch nutrition data when the user clicks Save
                String productName = nameField.getValue().trim();
                if (productName.isEmpty()) {
                    Notification.show(getTranslation("pro.name_noti"));
                    return;
                }

                // Fetch the nutrition data based on the user-provided product name
                String generatedNutritions = "";
                try {
                    getLanguage = getTranslation("getLanguage");
                    generatedNutritions = aiClient.getNutritions(productName, getLanguage);
                    System.out.println("Nutritions for " + productName + ": " + generatedNutritions); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.err.println("Error fetching nutrition data: " + ex.getMessage());
                    Notification.show(getTranslation("nutr.fetch_error"));
                    return;  // Exit if nutrition data couldn't be fetched
                }
                Product newProduct = new Product();
                newProduct.setName(nameField.getValue());
                newProduct.setQuantity(Integer.parseInt(quantityField.getValue()));
                newProduct.setPrice(Double.parseDouble(priceField.getValue()));
                
                newProduct.setNutritions(generatedNutritions);

                newProduct.setLanguageCode(languageCode);

                if (expirationDate.getValue() != null) {
                    long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate.getValue());
                    newProduct.setAmountOfDaysUntilExpiration((int) daysUntilExpiration);
                } else {
                    Notification.show(getTranslation("valid.exp_date"));
                }


                Inventory inventory = inventoryService.findById(selectedInventory.getId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));
                newProduct.setInventory(inventory);


                // Set Supplier and Category from TextFields
                String supplierName = supplierField.getValue().trim();
                String categoryName = categoryField.getValue().trim();

                if (supplierName.isEmpty() || categoryName.isEmpty()) {
                    Notification.show(getTranslation("valid.sup_cat"));
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
                updateGrid(currentInventory, currentLanguageCode);
                dialog.close();
                Notification.show(getTranslation("succ.pro_add"));
            } catch (NumberFormatException ex) {
                Notification.show(getTranslation("inv.qua_pri_add"));
            }
        });

        // Create a wrapper for the Category field and add spacing after it
        VerticalLayout categoryWrapper = new VerticalLayout(categoryField);
        categoryWrapper.setPadding(false);
        categoryWrapper.getStyle().set("margin-bottom", "15px");

        // Create a layout for form elements
        VerticalLayout formLayout = new VerticalLayout(nameField, quantityField, priceField, expirationDate, supplierField, categoryWrapper);
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Add Save button separately
        VerticalLayout contentLayout = new VerticalLayout(formLayout, saveButton);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(contentLayout);
        dialog.open();
    }

    // Displaying the Edit modal
    private void showEditProductDialog(Product product) {
        Dialog dialog = new Dialog();
        nameField = new TextField(getTranslation("name"));
        nameField.setValue(product.getName());

        quantityField = new TextField(getTranslation("quantity"));
        quantityField.setValue(String.valueOf(product.getQuantity()));

        priceField = new TextField(getTranslation("price"));
        priceField.setValue(String.valueOf(product.getPrice()));

        expirationDate = new DatePicker(getTranslation("exp_date"));

        supplierField = new TextField(getTranslation("supplier"));
        supplierField.setValue(product.getSupplier().getName());

        categoryField = new TextField(getTranslation("category"));
        categoryField.setValue(product.getCategory().getName());


        saveButton = new Button(getTranslation("save"), e -> {
            try {
                product.setName(nameField.getValue());
                product.setQuantity(Integer.parseInt(quantityField.getValue()));
                product.setPrice(Double.parseDouble(priceField.getValue()));
                if (expirationDate.getValue() != null) {
                    long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate.getValue());
                    product.setAmountOfDaysUntilExpiration((int) daysUntilExpiration);
                } else {
                    Notification.show(getTranslation("valid.exp_date"));
                }

                // Ensure an inventory is selected
                Inventory selectedInventory = product.getInventory();
                if (selectedInventory == null) {
                    Notification.show(getTranslation("inv.sel_noti"));
                    return;
                }
                product.setInventory(selectedInventory);

                // Fetch or Create Supplier & Category
                String supplierName = supplierField.getValue().trim();
                String categoryName = categoryField.getValue().trim();

                if (nameField.getValue().isEmpty()) {
                    Notification.show(getTranslation("inv.sup_add"));
                    return;
                }

                if (supplierName.isEmpty() || categoryName.isEmpty()) {
                    Notification.show(getTranslation("valid.sup_cat"));
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
                updateGrid(currentInventory, currentLanguageCode);
                dialog.close();
                Notification.show(getTranslation("succ.pro_add"));
            } catch (NumberFormatException ex) {
                Notification.show(getTranslation("inv.qua_pri_add"));
            }
        });

        // Wrapper for Category field to add bottom spacing
        VerticalLayout categoryWrapper = new VerticalLayout(categoryField);
        categoryWrapper.setPadding(false);
        categoryWrapper.getStyle().set("margin-bottom", "15px");

        // Layout for form fields (no extra spacing between them)
        VerticalLayout formLayout = new VerticalLayout(nameField, quantityField, priceField, expirationDate, supplierField, categoryWrapper);
        formLayout.setPadding(false);
        formLayout.setSpacing(false);

        // Center the Save button
        VerticalLayout buttonWrapper = new VerticalLayout(saveButton);
        buttonWrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        // Add everything to dialog
        dialog.add(formLayout, buttonWrapper);
        dialog.open();
    }

    // Deleting the Product from inventory and updates the grid
    private void deleteProduct(Product product) {
        productService.deleteProduct(product.getId());
        updateGrid(currentInventory, currentLanguageCode);
        Notification.show(getTranslation("succ.pro_del"));
    }

    // Searchbox for searching the Product by Name
    private void searchByName() {
        searchbar = new TextField();
        searchbar.setPlaceholder(getTranslation("search.pro"));
    
        searchButton = new Button(getTranslation("search"));
    
        searchButton.addClickListener(event -> {
            Inventory inventory = currentInventory;
    
            if (inventory == null) {
                Notification.show(getTranslation("inv.sel_noti"));
                return;
            }
    
            String searchText = searchbar.getValue().trim();
            String languageCode = UI.getCurrent().getLocale().getLanguage();
            if (!searchText.isEmpty()) {
                List<Product> products = productService.getProductsByName(inventory.getId(), searchText, languageCode);
                grid.setItems(products);
            } else {
                List<Product> products = productService.getProductsByInventory(inventory.getId(), languageCode);
                grid.setItems(products);
            }
        });
    
        // CSS
        searchbar.addClassName("searchbar");
        searchButton.addClassName("search-button");
    
        HorizontalLayout searchLayout = new HorizontalLayout(searchbar, searchButton);
        searchLayout.addClassName("search-layout");
        searchLayout.setWidthFull();
    
        add(searchLayout);
    }

    // Updating the grid according to the current Inventory and language
    public void updateGrid(Inventory selectedInventory, String currentLanguageCode) {
        currentInventory = selectedInventory;
        if (selectedInventory != null) {
            List<Product> products = getFilteredProducts(currentFilter, currentInventory);
            grid.setItems(products);
        } else {
            grid.setItems();
        }
    }

    private List<Product> getFilteredProducts(String filter, Inventory inventory) {
        String languageCode = UI.getCurrent().getLocale().getLanguage();
        List<Product> filteredProducts = productService.getProductsByInventory(inventory.getId(), languageCode);

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

    public void updateTexts() {
        allButton.setText(getTranslation("inventory.filter_all"));
        expiringButton.setText(getTranslation("inventory.filter_exp"));
        lowStockButton.setText(getTranslation("inventory.filter_low"));
        outOfStockButton.setText(getTranslation("inventory.filter_out"));
        // Update Grid column header for 'Name' column
        grid.getColumnByKey("name").setHeader(getTranslation("name"));
        // If you want to make sure every other column also gets translated, do something like this:
        grid.getColumnByKey("quantity").setHeader(getTranslation("quantity"));
        grid.getColumnByKey("price").setHeader(getTranslation("price"));
        grid.getColumnByKey("nutritions").setHeader(getTranslation("nutritions"));
        grid.getColumnByKey("days_until_expiration").setHeader(getTranslation("days_until_expiration"));
        grid.getColumnByKey("category").setHeader(getTranslation("category"));
        grid.getColumnByKey("supplier").setHeader(getTranslation("supplier"));

        searchbar.setPlaceholder(getTranslation("search.pro"));
        searchButton.setText(getTranslation("search"));
        addButton.setText(getTranslation("inventory.add_pro"));

        getLanguage = getTranslation("getLanguage");
    }
}
