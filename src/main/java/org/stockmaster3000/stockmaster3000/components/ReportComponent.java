package org.stockmaster3000.stockmaster3000.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.model.Report;
import org.stockmaster3000.stockmaster3000.security.SecurityService;
import org.stockmaster3000.stockmaster3000.service.ProductLogService;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ReportComponent extends VerticalLayout {

    @Autowired
    private OpenAIClient client;

    private InventorySelectorComponent inventorySelectorComponent;
    private ProductService productService;
    private ProductLogService productLogService;
    private ReportService reportService;
    private ReportSelectorComponent reportSelectorComponent;
    private SecurityService securityService;
    
    // Localazion
    private H3 topic;
    private TextArea resultTextArea;
    private Button button1;
    private Button button2;
    private Button button3;
    private String reportSavedNotification;
    private String notificationSelectInventory;
    private String getLanguage;

    public ReportComponent(OpenAIClient client, InventorySelectorComponent inventorySelectorComponent, 
    ProductService productService, ProductLogService productLogService, ReportService reportService, 
    ReportSelectorComponent reportSelectorComponent, SecurityService securityService) {

        this.client = client;
        this.inventorySelectorComponent = inventorySelectorComponent;
        this.reportSelectorComponent = reportSelectorComponent;
        this.productService = productService;
        this.productLogService = productLogService;
        this.reportService = reportService;
        this.securityService = securityService;

        // Giving the Report tab topic
        topic = new H3(getTranslation("reports.topic"));

        // Initializing the text area for displaying generated content
        resultTextArea = new TextArea(getTranslation("reports.resultTextArea"));
        resultTextArea.setWidthFull();
        resultTextArea.setHeightFull();
        resultTextArea.setReadOnly(true);

        // Initializing the buttons
        button1 = new Button(getTranslation("reports.button1"));
        button2 = new Button(getTranslation("reports.button2"));
        button3 = new Button(getTranslation("reports.button3"));

        getLanguage = getTranslation("getLanguage");

        // Local date
        LocalDate date = LocalDate.now();

        // Click listeners for each button
        button1.addClickListener(event -> {
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show(notificationSelectInventory);
                return;
            }

            // Fetching the products from inventory
            List<Product> products = productService.getProductsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            String currentMonth = date.getMonth().toString();
            try {
                resultTextArea.setValue("");
                String plan = client.generateInventoryPlanningSuggestionsAndMealPlans(currentIngredients, currentMonth, getLanguage);
                System.out.println("GHAFAASKNADKNEW CHECK HERE: " + getLanguage);
                resultTextArea.setValue(plan);
                reportService.saveReport(plan, currentInventory);
                System.out.println(reportSavedNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        button2.addClickListener(event -> {
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show(notificationSelectInventory);
                return;
            }

            List<Map<String, Object>> products = productLogService.getProductDetailsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            try {
                resultTextArea.setValue("");
                String analysedInventory = client.generateInventoryHealthinessAnalysis(currentIngredients, getLanguage);
                resultTextArea.setValue(analysedInventory);
                reportService.saveReport(analysedInventory, currentInventory);
                System.out.println(reportSavedNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        button3.addClickListener(event -> {
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show(notificationSelectInventory);
                return;
            }
            List<Product> products = productService.getProductsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            try {
                resultTextArea.setValue("");
                String mealPlan = client.generateMealPlanBasedOnCurrentInventoryIngredients(currentIngredients, getLanguage);
                resultTextArea.setValue(mealPlan);
                reportService.saveReport(mealPlan, currentInventory);
                System.out.println(reportSavedNotification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        reportSelectorComponent.addClickListener(event -> {
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show(notificationSelectInventory);
                return;
            }
            Report selectedReport = reportSelectorComponent.getSelectedReport(); 
            try {
                resultTextArea.setValue("");
                String reportDetails = selectedReport.getSummary(); 
                resultTextArea.setValue(reportDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // Add buttons to the layout
        add(topic, button1, button2, button3, reportSelectorComponent, resultTextArea);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }

    public void updateTexts() {
        topic.setText(getTranslation("reports.topic"));
        resultTextArea.setLabel(getTranslation("reports.resultTextArea"));
        button1.setText(getTranslation("reports.button1"));
        button2.setText(getTranslation("reports.button2"));
        button3.setText(getTranslation("reports.button3"));
        
        // Update notification messages
        reportSavedNotification = getTranslation("reports.reportSavedNotification");
        notificationSelectInventory = getTranslation("reports.notificationSelectInventory");
        getLanguage = getTranslation("getLanguage");
    }
}
