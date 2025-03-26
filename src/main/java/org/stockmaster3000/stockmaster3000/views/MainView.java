package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;
import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryChartComponent;
import org.stockmaster3000.stockmaster3000.components.InventoryComponent;
import org.stockmaster3000.stockmaster3000.components.InventorySelectorComponent;
import org.stockmaster3000.stockmaster3000.components.ReportComponent;
import org.stockmaster3000.stockmaster3000.components.ReportSelectorComponent;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.service.*;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("main")
@PermitAll
public class MainView extends VerticalLayout implements LocaleChangeObserver {

    private final InventoryChartComponent inventoryChartComponent;
    private final InventoryComponent inventoryComponent;
    private final InventorySelectorComponent inventorySelectorComponent;
    private final HeaderComponent headerComponent;
    private final ReportComponent reportComponent;
    private final OpenAIClient client;
    private final ReportSelectorComponent reportSelectorComponent;
    Tab tab1;
    Tab tab2;
    Tab tab3;

    public MainView(SecurityService securityService, InventoryService inventoryService, 
                    ProductService productService, CategoryService categoryService, 
                    SupplierService supplierService, OpenAIClient client, 
                    ProductLogService productLogService, ReportService reportService) {

        this.client = client;
        
        // Instantiate the reusable components
        inventoryChartComponent = new InventoryChartComponent(securityService, productService);
        inventoryComponent = new InventoryComponent(securityService, inventoryService, productService, categoryService, supplierService);
        inventorySelectorComponent = new InventorySelectorComponent(securityService, inventoryService);
        reportSelectorComponent = new ReportSelectorComponent(securityService, reportService, inventorySelectorComponent);
        headerComponent = new HeaderComponent(securityService);
        reportComponent = new ReportComponent(client, inventorySelectorComponent, productService, productLogService, reportService, reportSelectorComponent, securityService);

        // Set to take up all the available space 100%
        setSizeFull();
        setSpacing(true); // Adds spacing between elements

        // Add the header and inventory selector at the top
        add(headerComponent, inventorySelectorComponent);

        // Set up inventory selection listener
        inventorySelectorComponent.setSelectionListener(selectedInventory -> {
            inventoryComponent.updateGrid(selectedInventory); // Update InventoryComponent
            inventoryChartComponent.updateCharts(selectedInventory); // Update ALL charts
        });

        // Create tabs
        Tabs tabs = new Tabs();
        tab1 = new Tab(getTranslation("dashboard"));
        tab2 = new Tab(getTranslation("insights"));
        tab3 = new Tab(getTranslation("reports"));

        // Create the content for each tab
        VerticalLayout dashboardContent = new VerticalLayout(inventoryComponent);
        VerticalLayout insightsContent = new VerticalLayout(inventoryChartComponent);
        VerticalLayout reportContent = new VerticalLayout(reportComponent);

        // Add margin and padding for better UI
        dashboardContent.getStyle().set("padding", "20px");
        insightsContent.getStyle().set("padding", "20px");
        reportContent.getStyle().set("padding", "20px");

        // Initially show the dashboard content and hide the insights & reports content
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

                // Update the charts with the currently selected inventory
                Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
                inventoryChartComponent.updateCharts(selectedInventory);

            } else if (event.getSelectedTab().equals(tab3)) {
                // Show report content and hide other content
                dashboardContent.setVisible(false);
                insightsContent.setVisible(false);
                reportContent.setVisible(true);
            }
        });

        // Add the tabs and content to the layout
        add(tabs, dashboardContent, insightsContent, reportContent);

        // Select the default tab
        tabs.setSelectedTab(tab1);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        // Update text when the locale changes
        this.tab1.setLabel(getTranslation("dashboard"));
        this.tab2.setLabel(getTranslation("insights"));
        this.tab3.setLabel(getTranslation("reports"));
        headerComponent.updateTexts();
        inventoryComponent.updateTexts();
    }
}
