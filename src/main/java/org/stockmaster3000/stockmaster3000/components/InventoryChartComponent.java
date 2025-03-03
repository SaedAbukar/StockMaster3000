package org.stockmaster3000.stockmaster3000.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.security.SecurityService;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Div;


public class InventoryChartComponent extends VerticalLayout {

    private final ProductService productService;
    private DoughnutChart doughnutChart;
    private BarChart spendingChart;
    private LineChart expirationChart;

    public InventoryChartComponent(SecurityService securityService, ProductService productService) {
        this.productService = productService;

        H1 componentTopic = new H1("Inventory Insights");
        componentTopic.getStyle().set("text-align", "center");
        add(componentTopic);

        // Create a layout for the top row charts
        HorizontalLayout topChartsLayout = new HorizontalLayout();
        topChartsLayout.setWidthFull();
        topChartsLayout.setSpacing(true);
        topChartsLayout.getStyle().set("gap", "16px");

        // Chart Containers
        doughnutChart = new DoughnutChart(new String[]{"No Data"}, new int[]{1});
        spendingChart = new BarChart(new String[]{"No Data"}, new double[]{0.0});
        expirationChart = new LineChart(new String[]{"No Data"}, new int[]{0});

        // Container Style Method
        VerticalLayout doughnutContainer = createChartContainer("Food Category Distribution", doughnutChart);
        VerticalLayout spendingContainer = createChartContainer("Spending Per Food Item", spendingChart);
        VerticalLayout expirationContainer = createChartContainer("Food Expiration Timeline", expirationChart);
        
        // Add charts to layouts
        topChartsLayout.add(doughnutContainer, spendingContainer);
        add(topChartsLayout, expirationContainer);

        // Set spacing between components
        setSpacing(true);
        setPadding(true);
    }

    /**
     * Creates a styled container for each chart.
     */
    private VerticalLayout createChartContainer(String title, Div chart) {
        VerticalLayout container = new VerticalLayout();
        H3 chartTitle = new H3(title);
        chartTitle.getStyle().set("text-align", "center");
        
        container.add(chartTitle, chart);
        container.getStyle().set("border", "1px solid #e0e0e0");
        container.getStyle().set("border-radius", "8px");
        container.getStyle().set("padding", "16px");
        container.getStyle().set("background-color", "#fafafa");
        container.getStyle().set("box-shadow", "0px 4px 8px rgba(0, 0, 0, 0.1)");
        container.setWidth("48%");
        
        return container;
    }

    /**
     * Updates all charts based on the selected inventory.
     */
    public void updateCharts(Inventory selectedInventory) {
        if (selectedInventory != null) {
            Map<Category, Integer> productData = productService.getProductDataByInventory(selectedInventory.getId());
            updateCategoryChart(productData);

            List<Product> products = productService.getProductsByInventory(selectedInventory.getId());
            updateSpendingChart(products);
            updateExpirationChart(products);
        } else {
            clearCharts();
        }
    }

    private void clearCharts() {
        doughnutChart.updateChart(new String[]{"No Data"}, new int[]{1});
        spendingChart.updateChart(new String[]{"No Data"}, new double[]{0.0});
        expirationChart.updateChart(new String[]{"No Data"}, new int[]{0});
    }

    /**
     * Updates the Food Category Distribution chart.
     */
    private void updateCategoryChart(Map<Category, Integer> productData) {
        if (productData.isEmpty()) {
            doughnutChart.updateChart(new String[]{"No Data"}, new int[]{1});
        } else {
            String[] productTypes = productData.keySet().stream().map(Category::getName).toArray(String[]::new);
            int[] productCounts = productData.values().stream().mapToInt(Integer::intValue).toArray();
            doughnutChart.updateChart(productTypes, productCounts);
        }
    }

    /**
     * Updates the Spending Per Food Item chart.
     */
    private void updateSpendingChart(List<Product> products) {
        if (products == null || products.isEmpty()) {
            spendingChart.updateChart(new String[]{"No Data"}, new double[]{0.0});
            return;
        }

        Map<String, Double> productSpending = new HashMap<>();
        for (Product product : products) {
            if (product.getName() != null && product.getPrice() != null && product.getQuantity() != null) {
                productSpending.put(product.getName(), product.getQuantity() * product.getPrice());
            }
        }

        if (productSpending.isEmpty()) {
            spendingChart.updateChart(new String[]{"No Data"}, new double[]{0.0});
            return;
        }

        List<Map.Entry<String, Double>> topSpending = productSpending.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());

        String[] labels = topSpending.stream().map(Map.Entry::getKey).toArray(String[]::new);
        double[] values = topSpending.stream().mapToDouble(Map.Entry::getValue).toArray();
        spendingChart.updateChart(labels, values);
    }

    /**
     * Updates the Food Expiration Timeline chart.
     */
    private void updateExpirationChart(List<Product> products) {
        if (products == null || products.isEmpty()) {
            expirationChart.updateChart(new String[]{"No Data"}, new int[]{0});
            return;
        }

        Map<String, Integer> expirationDays = new HashMap<>();
        for (Product product : products) {
            if (product.getName() != null && product.getAmountOfDaysUntilExpiration() != null) {
                expirationDays.put(product.getName(), product.getAmountOfDaysUntilExpiration());
            }
        }

        if (expirationDays.isEmpty()) {
            expirationChart.updateChart(new String[]{"No Data"}, new int[]{0});
            return;
        }

        List<Map.Entry<String, Integer>> sortedByExpiration = expirationDays.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(10)
            .collect(Collectors.toList());

        String[] labels = sortedByExpiration.stream().map(Map.Entry::getKey).toArray(String[]::new);
        int[] values = sortedByExpiration.stream().mapToInt(Map.Entry::getValue).toArray();
        expirationChart.updateChart(labels, values);
    }
}
