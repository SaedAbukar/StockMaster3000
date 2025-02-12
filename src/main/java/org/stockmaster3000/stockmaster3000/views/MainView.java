package org.stockmaster3000.stockmaster3000.views;

import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryChartComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryComponent;
import org.stockmaster3000.stockmaster3000.components.InventorySelectorComponent;
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

    public MainView(SecurityService securityService, InventoryService inventoryService, 
                    ProductService productService, CategoryService categoryService, 
                    SupplierService supplierService) {
        // Instantiate the reusable components
        inventoryChartComponent = new InventoryChartComponent(securityService, productService, inventoryService);
        inventoryComponent = new InventoryComponent(securityService, inventoryService, productService, categoryService, supplierService);
        inventorySelectorComponent = new InventorySelectorComponent(securityService, inventoryService);
        HeaderComponent headerComponent = new HeaderComponent(securityService);

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

        // Create the content for each tab
        VerticalLayout dashboardContent = new VerticalLayout(inventoryComponent);
        VerticalLayout insightsContent = new VerticalLayout(inventoryChartComponent);

        // Initially show the dashboard content and hide the insights content
        dashboardContent.setVisible(true);
        insightsContent.setVisible(false);

        // Add tabs to the Tabs component
        tabs.add(tab1, tab2);

        // Set up tab click listener to change content
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().equals(tab1)) {
                // Show dashboard content and hide insights content
                dashboardContent.setVisible(true);
                insightsContent.setVisible(false);
            } else if (event.getSelectedTab().equals(tab2)) {
                // Show insights content and hide dashboard content
                dashboardContent.setVisible(false);
                insightsContent.setVisible(true);

                // Update the chart with the currently selected inventory
                Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
                // Making sure that the application uses the correct inventory
                System.out.println("Switching to Insights tab. Selected Inventory: " + (selectedInventory != null ? selectedInventory.getName() : "null"));
                inventoryChartComponent.updateDoughnutChart(selectedInventory);
            }
        });

        // Add the tabs and content to the layout
        add(headerComponent, inventorySelectorComponent, tabs, dashboardContent, insightsContent);

        // Select the default tab
        tabs.setSelectedTab(tab1);
    }
}