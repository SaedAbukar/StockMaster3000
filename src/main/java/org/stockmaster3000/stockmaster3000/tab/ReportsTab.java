package org.stockmaster3000.stockmaster3000.tab;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.client.OpenAiClient;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.ProductLogService;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.ReportService;

/**
 * The ReportsTab allows users to generate and view inventory-based reports.
 */
public class ReportsTab extends VerticalLayout {

  @Autowired
  private OpenAiClient client;

  private final InventoryCombox inventorySelectorComponent;
  private final ProductService productService;
  private final ProductLogService productLogService;
  private final ReportService reportService;
  private final ReportCombox reportSelectorComponent;
  private final SecurityService securityService;

  private H3 topic;
  private TextArea resultTextArea;
  private Button button1;
  private Button button2;
  private Button button3;
  //  String reportSavedNotification;
  private String notificationSelectInventory;
  private String getLanguage;

  /**
   * Constructs the ReportsTab with dependencies.
   *
   * @param client the OpenAI client
   * @param inventorySelectorComponent inventory selector component
   * @param productService product data service
   * @param productLogService service for product log data
   * @param reportService report persistence service
   * @param reportSelectorComponent report selection dropdown
   * @param securityService authenticated user provider
   */
  public ReportsTab(OpenAiClient client, InventoryCombox inventorySelectorComponent,
                    ProductService productService, ProductLogService productLogService,
                    ReportService reportService, ReportCombox reportSelectorComponent,
                    SecurityService securityService) {

    this.client = client;
    this.inventorySelectorComponent = inventorySelectorComponent;
    this.productService = productService;
    this.productLogService = productLogService;
    this.reportService = reportService;
    this.reportSelectorComponent = reportSelectorComponent;
    this.securityService = securityService;

    topic = new H3(getTranslation("reports.topic"));

    resultTextArea = new TextArea(getTranslation("reports.resultTextArea"));
    resultTextArea.setWidthFull();
    resultTextArea.setHeightFull();
    resultTextArea.setReadOnly(true);

    button1 = new Button(getTranslation("reports.button1"));
    button2 = new Button(getTranslation("reports.button2"));
    button3 = new Button(getTranslation("reports.button3"));

    getLanguage = getTranslation("getLanguage");
    LocalDate date = LocalDate.now();

    setupButtonListeners(date);
    setupReportSelectorListener();

    add(topic, button1, button2, button3, reportSelectorComponent, resultTextArea);
    setJustifyContentMode(JustifyContentMode.CENTER);
    setSizeFull();
  }

  /**
   * Initializes listeners for report generation buttons.
   */
  private void setupButtonListeners(LocalDate date) {
    button1.addClickListener(event -> {
      Inventory inventory = inventorySelectorComponent.getSelectedInventory();
      if (inventory == null) {
        Notification.show(notificationSelectInventory);
        return;
      }

      String languageCode = UI.getCurrent().getLocale().getLanguage();
      List<Product> products = productService
          .getProductsByInventory(inventory.getId(), languageCode);

      try {
        String result = client.generateInventoryPlanningSuggestionsAndMealPlans(
            products.toString(), date.getMonth().toString(), getLanguage);

        resultTextArea.setValue(result);
        reportService.saveReport(result, inventory);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    button2.addClickListener(event -> {
      Inventory inventory = inventorySelectorComponent.getSelectedInventory();
      if (inventory == null) {
        Notification.show(notificationSelectInventory);
        return;
      }

      List<Map<String, Object>> products = productLogService
          .getProductDetailsByInventory(inventory.getId());

      try {
        String result = client.generateInventoryHealthinessAnalysis(
            products.toString(), getLanguage);

        resultTextArea.setValue(result);
        reportService.saveReport(result, inventory);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    button3.addClickListener(event -> {
      Inventory inventory = inventorySelectorComponent.getSelectedInventory();
      if (inventory == null) {
        Notification.show(notificationSelectInventory);
        return;
      }

      String languageCode = UI.getCurrent().getLocale().getLanguage();
      List<Product> products = productService
          .getProductsByInventory(inventory.getId(), languageCode);

      try {
        String result = client.generateMealPlanBasedOnCurrentInventoryIngredients(
            products.toString(), getLanguage);

        resultTextArea.setValue(result);
        reportService.saveReport(result, inventory);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Handles report selection from the dropdown.
   */
  private void setupReportSelectorListener() {
    reportSelectorComponent.addClickListener(event -> {
      Inventory inventory = inventorySelectorComponent.getSelectedInventory();
      if (inventory == null) {
        Notification.show(notificationSelectInventory);
        return;
      }

      Report report = reportSelectorComponent.getSelectedReport();
      if (report != null) {
        resultTextArea.setValue(report.getSummary());
      }
    });
  }

  /**
   * Updates translatable UI strings.
   */
  public void updateTexts() {
    topic.setText(getTranslation("reports.topic"));
    resultTextArea.setLabel(getTranslation("reports.resultTextArea"));
    button1.setText(getTranslation("reports.button1"));
    button2.setText(getTranslation("reports.button2"));
    button3.setText(getTranslation("reports.button3"));
    // reportSavedNotification = getTranslation("reports.reportSavedNotification");
    notificationSelectInventory = getTranslation("reports.notificationSelectInventory");
    getLanguage = getTranslation("getLanguage");
  }
}
