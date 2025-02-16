package org.stockmaster3000.stockmaster3000.components;

import java.util.Map;

import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;

public class InventoryChartComponent extends VerticalLayout {

    private final ProductService productService;
    private DoughnutChart doughnutChart;

    // Component Constructor
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
    public InventoryChartComponent(SecurityService securityService, ProductService productService) {
        this.productService = productService;

        H1 componentTopic = new H1("Inventory Insights");
        add(componentTopic);

        // Add a subtitle for the doughnut chart
        H3 doughnutTopic = new H3("Distribution of Inventory Categories");
        add(doughnutTopic);

        // Initialize the doughnut chart with empty data
        doughnutChart = new DoughnutChart(new String[]{"No Data"}, new int[]{1});
        add(doughnutChart);
    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void updateDoughnutChart(Inventory selectedInventory) {
        if (selectedInventory != null) {
            // Fetch data for the selected inventory
            Map<Category, Integer> productData = productService.getProductDataByInventory(selectedInventory.getId());
    
            if (productData.isEmpty()) {
                // If there are no products, display "No Data"
                doughnutChart.updateChart(new String[]{"No Data"}, new int[]{1});
            } else {
                // Process and display the product data
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
            }
        } else {
            // If no inventory is selected, clear the chart
            doughnutChart.updateChart(new String[]{"No Data"}, new int[]{1});
        }
    }
}