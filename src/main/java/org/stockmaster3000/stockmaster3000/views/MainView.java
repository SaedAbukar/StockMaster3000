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

/**
 * Main view of the application that contains tabs for Dashboard, Insights, and Reports.
 *
 * <p>It provides language switching and integrates all major components.
 */
@Route("main")
@PermitAll
public class MainView extends VerticalLayout implements LocaleChangeObserver {

  private final InsightsTab inventoryChartComponent;
  private final DashboardTab inventoryComponent;
  private final InventoryCombox inventorySelectorComponent;
  private final HeaderComponent headerComponent;
  private final ReportsTab reportComponent;
  private final ReportCombox reportSelectorComponent;

  private final Tab tab1;
  private final Tab tab2;
  private final Tab tab3;

  private final String currentLanguageCode = UI.getCurrent().getLocale().getLanguage();

  /**
   * Constructs the MainView with all required services and UI components.
   *
   * @param securityService    security service
   * @param inventoryService   inventory service
   * @param productService     product service
   * @param categoryService    category service
   * @param supplierService    supplier service
   * @param client             OpenAI client
   * @param productLogService  product log service
   * @param reportService      report service
   */
  public MainView(SecurityService securityService, InventoryService inventoryService,
                  ProductService productService, CategoryService categoryService,
                  SupplierService supplierService, OpenAiClient client,
                  ProductLogService productLogService, ReportService reportService) {

    headerComponent = new HeaderComponent(securityService);
    inventoryChartComponent = new InsightsTab(securityService, productService);
    inventorySelectorComponent = new InventoryCombox(securityService, inventoryService);
    reportSelectorComponent = new ReportCombox(securityService, reportService,
        inventorySelectorComponent);
    inventoryComponent = new DashboardTab(headerComponent, securityService, inventoryService,
        productService, categoryService, supplierService);
    reportComponent = new ReportsTab(client, inventorySelectorComponent, productService,
        productLogService, reportService, reportSelectorComponent, securityService);

    setSizeFull();
    setSpacing(true);
    add(headerComponent, inventorySelectorComponent);

    inventorySelectorComponent.setSelectionListener(selectedInventory -> {
      inventoryComponent.updateGrid(selectedInventory, currentLanguageCode);
      inventoryChartComponent.updateCharts(selectedInventory);
    });

    tab1 = new Tab(getTranslation("dashboard"));
    tab2 = new Tab(getTranslation("insights"));
    tab3 = new Tab(getTranslation("reports"));

    VerticalLayout dashboardContent = new VerticalLayout(inventoryComponent);
    VerticalLayout insightsContent = new VerticalLayout(inventoryChartComponent);
    VerticalLayout reportContent = new VerticalLayout(reportComponent);

    dashboardContent.getStyle().set("padding", "20px");
    insightsContent.getStyle().set("padding", "20px");
    reportContent.getStyle().set("padding", "20px");

    dashboardContent.setVisible(true);
    insightsContent.setVisible(false);
    reportContent.setVisible(false);

    Tabs tabs = new Tabs();
    tabs.add(tab1, tab2, tab3);

    tabs.addSelectedChangeListener(event -> {
      boolean isDashboard = event.getSelectedTab().equals(tab1);
      boolean isInsights = event.getSelectedTab().equals(tab2);
      boolean isReports = event.getSelectedTab().equals(tab3);

      dashboardContent.setVisible(isDashboard);
      insightsContent.setVisible(isInsights);
      reportContent.setVisible(isReports);

      if (isInsights) {
        Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
        inventoryChartComponent.updateCharts(selectedInventory);
      }
    });

    add(tabs, dashboardContent, insightsContent, reportContent);
    tabs.setSelectedTab(tab1);
  }

  /**
   * Handles text updates when the locale is changed.
   *
   * @param localeChangeEvent the locale change event
   */
  @Override
  public void localeChange(LocaleChangeEvent localeChangeEvent) {
    tab1.setLabel(getTranslation("dashboard"));
    tab2.setLabel(getTranslation("insights"));
    tab3.setLabel(getTranslation("reports"));

    headerComponent.updateTexts();
    inventoryComponent.updateTexts();
    inventorySelectorComponent.updateTexts();
    inventoryChartComponent.updateTexts();

    Inventory selectedInventory = inventorySelectorComponent.getSelectedInventory();
    inventoryChartComponent.updateCharts(selectedInventory);
    reportComponent.updateTexts();
    reportSelectorComponent.updateTexts();
  }
}
