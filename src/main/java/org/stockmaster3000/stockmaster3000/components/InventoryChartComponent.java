package org.stockmaster3000.stockmaster3000.components;

import java.util.Map;

import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InventoryChartComponent extends VerticalLayout {

    private final ProductService productService;
    private final InventoryService inventoryService;
    private DoughnutChart doughnutChart;

    public InventoryChartComponent(SecurityService securityService, ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;

        // Initialize the doughnut chart with empty data
        doughnutChart = new DoughnutChart(new String[]{}, new int[]{});
        add(doughnutChart);
    }

    public void updateDoughnutChart(Inventory selectedInventory) {
        if (selectedInventory != null) {
            // Fetch data for the selected inventory
            Map<Category, Integer> productData = productService.getProductDataByInventory(selectedInventory.getId());
    
            String[] productTypes = new String[productData.size()];
            int[] productCounts = new int[productData.size()];
            int index = 0;
    
            for (Map.Entry<Category, Integer> entry : productData.entrySet()) {
                productTypes[index] = entry.getKey().getName();
                productCounts[index] = entry.getValue();
                index++;
            }

            // Update the doughnut chart with the new data
            doughnutChart.updateChart(productTypes, productCounts);
        } else {
            // Clear the chart if no inventory is selected
            doughnutChart.updateChart(new String[]{}, new int[]{});
        }
    }
}