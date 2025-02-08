package org.stockmaster3000.stockmaster3000.views;

import java.util.List;
import java.util.Map;

import org.stockmaster3000.stockmaster3000.components.DoughnutChart;
import org.stockmaster3000.stockmaster3000.service.InventoryService;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("insights")  // Maps this UI to the root URL
@PermitAll  // Allows all users to access
public class InsightView extends VerticalLayout {

    private final ProductService productService;
    private ComboBox<Inventory> inventoryComboBox;
    private final SecurityService securityService;
    private final InventoryService inventoryService;

    // ---------------------------------------------------------------------------------------------------
    

    public InsightView(ProductService productService, SecurityService securityService, InventoryService inventoryService) {
        this.productService = productService;
        this.securityService = securityService;
        this.inventoryService = inventoryService;

        createInventorySelector();
    }

    // ---------------------------------------------------------------------------------------------------

    private void createInventorySelector() {
        inventoryComboBox = new ComboBox<>("Select Inventory");
        inventoryComboBox.setItemLabelGenerator(Inventory::getName);

        String username = getCurrentUsername();
        List<Inventory> inventories = inventoryService.getAllInventoriesByUser(username);
        inventoryComboBox.setItems(inventories);

        // Handle inventory selection change
        inventoryComboBox.addValueChangeListener(event -> updateChart());
        add(inventoryComboBox);
    }

    private String getCurrentUsername() {
        return securityService.getAuthenticatedUser().getUsername();
    }

    private void updateChart() {
        Inventory selectedInventory = inventoryComboBox.getValue();
        if (selectedInventory != null) {
            // Fetch product data for the selected inventory
            Map<Category, Integer> productData = productService.getProductDataByInventory(selectedInventory.getId());

            // Prepare data for the DoughnutChart
            String[] productTypes = new String[productData.size()];
            int[] productCounts = new int[productData.size()];
            int index = 0;

            for (Map.Entry<Category, Integer> entry : productData.entrySet()) {
                productTypes[index] = entry.getKey().getName();
                productCounts[index] = entry.getValue();
                index++;
            }

            // Create a DoughnutChart and add it to the layout
            DoughnutChart chart = new DoughnutChart(productTypes, productCounts);
            add(chart);
        } else {
            // Handle the case when no inventory is selected
            Notification.show("Please select an inventory.");
        }
    }
}
