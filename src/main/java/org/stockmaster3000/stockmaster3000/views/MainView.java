package org.stockmaster3000.stockmaster3000.views;

import org.stockmaster3000.stockmaster3000.client.OpenAIClient;
import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryChartComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryComponent;
import org.stockmaster3000.stockmaster3000.components.InventorySelectorComponent;
import org.stockmaster3000.stockmaster3000.components.ReportComponent;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.service.CategoryService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.SupplierService;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.security.PermitAll;

@Route("main")
@PermitAll
public class MainView extends VerticalLayout {

    private final InventoryChartComponent inventoryChartComponent;
    private final InventoryComponent inventoryComponent;
    private final InventorySelectorComponent inventorySelectorComponent;
    private final HeaderComponent headerComponent;
    private final ReportComponent reportComponent;
    private final OpenAIClient client;

    public MainView(SecurityService securityService, InventoryService inventoryService, 
                    ProductService productService, CategoryService categoryService, 
                    SupplierService supplierService, OpenAIClient client) {

        this.client = client;
        
        // Instantiate the reusable components
        inventoryChartComponent = new InventoryChartComponent(securityService, productService, inventoryService);
        inventoryComponent = new InventoryComponent(securityService, inventoryService, productService, categoryService, supplierService);
        inventorySelectorComponent = new InventorySelectorComponent(securityService, inventoryService);
        headerComponent = new HeaderComponent(securityService);
        reportComponent = new ReportComponent(client);

        // Set to take up all the available space 100%
        setSizeFull();

        // Add the header and inventory selector at the top
        add(headerComponent, inventorySelectorComponent);

        // Set up inventory selection listener
        inventorySelectorComponent.setSelectionListener(selectedInventory -> {
            inventoryComponent.updateGrid(selectedInventory); // Update InventoryComponent
            inventoryChartComponent.updateDoughnutChart(selectedInventory); // Update InventoryChartComponent
        });

        // Create tabs
        Tabs tabs = new Tabs();
        Tab tab1 = new Tab("Dashboard");
        Tab tab2 = new Tab("Insights");
        Tab tab3 = new Tab("Reports");

        // Create the content for each tab
        VerticalLayout dashboardContent = new VerticalLayout(inventoryComponent);
        VerticalLayout insightsContent = new VerticalLayout(inventoryChartComponent);
        VerticalLayout reportContent = new VerticalLayout(reportComponent);

        // Initially show the dashboard content and hide the insights content
        dashboardContent.setVisible(true);
        insightsContent.setVisible(false);
        reportContent.setVisible(false);

        // Add tabs to the Tabs component
        tabs.add(tab1, tab2, tab3);

        // Set up tab click listener to change content
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(tab1)) {
                // Show dashboard content and hide other content
                dashboardContent.setVisible(true);
                insightsContent.setVisible(false);
                reportContent.setVisible(false);

            } else if (event.getSelectedTab().equals(tab2)) {
                // Show insights content and hide other content
                dashboardContent.setVisible(false);
                insightsContent.setVisible(true);
                reportContent.setVisible(false);

                // Update the chart with the currently selected inventory
                Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
                inventoryChartComponent.updateDoughnutChart(selectedInventory);

            } else if (event.getSelectedTab().equals(tab3)) {
                // Show report content and hide other content
                dashboardContent.setVisible(false);
                insightsContent.setVisible(false);
                reportContent.setVisible(true);
            }
        });

        // Add the tabs and content to the layout
        add(headerComponent, inventorySelectorComponent, tabs, dashboardContent, insightsContent, reportContent);

        // Select the default tab
        tabs.setSelectedTab(tab1);
    }
}