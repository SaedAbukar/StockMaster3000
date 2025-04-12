package org.stockmaster3000.stockmaster3000.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.stockmaster3000.stockmaster3000.client.OpenAiClient;
import org.stockmaster3000.stockmaster3000.components.HeaderComponent;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.CategoryService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;
import org.stockmaster3000.stockmaster3000.service.ProductLogService;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.ReportService;
import org.stockmaster3000.stockmaster3000.service.SupplierService;
import org.stockmaster3000.stockmaster3000.tab.DashboardTab;
import org.stockmaster3000.stockmaster3000.tab.InsightsTab;
import org.stockmaster3000.stockmaster3000.tab.InventoryCombox;
import org.stockmaster3000.stockmaster3000.tab.ReportCombox;
import org.stockmaster3000.stockmaster3000.tab.ReportsTab;

@Route("main")
@PermitAll
public class MainView extends VerticalLayout implements LocaleChangeObserver {

  private final InsightsTab inventoryChartComponent;
  private final DashboardTab inventoryComponent;
  private final InventoryCombox inventorySelectorComponent;
  private final HeaderComponent headerComponent;
  private final ReportsTab reportComponent;
  private final ReportCombox reportSelectorComponent;


  String currentLanguageCode = UI.getCurrent().getLocale().getLanguage();

  Tab tab1;
  Tab tab2;
  Tab tab3;

  public MainView(SecurityService securityService, InventoryService inventoryService,
                  ProductService productService, CategoryService categoryService,
                  SupplierService supplierService, OpenAiClient client,
                  ProductLogService productLogService, ReportService reportService) {

    // Instantiate the reusable components
    headerComponent = new HeaderComponent(securityService);
    inventoryChartComponent = new InsightsTab(securityService, productService);
    inventorySelectorComponent = new InventoryCombox(securityService, inventoryService);
    reportSelectorComponent = new ReportCombox(securityService, reportService,
        inventorySelectorComponent);
    inventoryComponent = new DashboardTab(headerComponent, securityService, inventoryService,
        productService, categoryService, supplierService);
    reportComponent = new ReportsTab(client, inventorySelectorComponent, productService,
        productLogService, reportService, reportSelectorComponent, securityService);

    // Set to take up all the available space 100%
    setSizeFull();
    setSpacing(true); // Adds spacing between elements

    // Add the header and inventory selector at the top
    add(headerComponent, inventorySelectorComponent);

    // Set up inventory selection listener
    inventorySelectorComponent.setSelectionListener(selectedInventory -> {
      inventoryComponent.updateGrid(selectedInventory, currentLanguageCode); // Update
      // InventoryComponent
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
    Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();

    // Update text when the locale changes
    this.tab1.setLabel(getTranslation("dashboard"));
    this.tab2.setLabel(getTranslation("insights"));
    this.tab3.setLabel(getTranslation("reports"));
    headerComponent.updateTexts();
    inventoryComponent.updateTexts();
    inventorySelectorComponent.updateTexts();
    inventoryChartComponent.updateTexts();
    inventoryChartComponent.updateCharts(selectedInventory);
    reportComponent.updateTexts();
    reportSelectorComponent.updateTexts();
  }
}
