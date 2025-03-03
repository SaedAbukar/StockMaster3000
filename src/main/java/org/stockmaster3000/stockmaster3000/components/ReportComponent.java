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
import org.stockmaster3000.stockmaster3000.service.ProductLogService;
import org.stockmaster3000.stockmaster3000.service.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ReportComponent extends VerticalLayout {

    @Autowired
    private OpenAIClient client;
    private InventorySelectorComponent inventorySelectorComponent;
    private ProductService productService;
    private ProductLogService productLogService;

    public ReportComponent(OpenAIClient client, InventorySelectorComponent inventorySelectorComponent, ProductService productService, ProductLogService productLogService) {
        this.client = client;
        this.inventorySelectorComponent = inventorySelectorComponent;
        this.productService = productService;
        this.productLogService = productLogService;

        // Giving the Report tab topic
        H3 topic = new H3("Generate Reports with AI!");

        // Initializing the text area for displaying generated content
        TextArea resultTextArea = new TextArea("AI Generated Report");
        resultTextArea.setWidthFull();
        resultTextArea.setHeightFull();
        resultTextArea.setReadOnly(true);

        // Initializing the buttons
        Button button1 = new Button("Get shopping list for the next 7 days + Meal Plan");
        Button button2 = new Button("Analyze your past 30 days ingredients healthiness!");
        Button button3 = new Button("Generate meal suggestions based on the current fridge ingredients!");
        Button PDFGeneratorButton = new Button("Download as PDF");
        LocalDate date = LocalDate.now();

        // Click listeners for each button
        button1.addClickListener(event -> {
            // TODO: Implement the query to the database
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show("Select an Inventory");
                return;
            }

            List<Product> products = productService.getProductsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            String currentMonth = date.getMonth().toString();
            try {
                resultTextArea.setValue("");
                String plan = client.generateInventoryPlanningSuggestionsAndMealPlans(currentIngredients, currentMonth);
                resultTextArea.setValue(plan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        button2.addClickListener(event -> {
            // TODO: Implement the query to the database
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show("Select an Inventory");
                return;
            }
            List<Map<String, Object>> products = productLogService.getProductDetailsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            try {
                resultTextArea.setValue("");
                String analysedInventory = client.generateInventoryHealthinessAnalysis(currentIngredients);
                resultTextArea.setValue(analysedInventory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        button3.addClickListener(event -> {
            // TODO: Implement the query to the database
            Inventory currentInventory = inventorySelectorComponent.getSelectedInventory();
            if (currentInventory == null) {
                Notification.show("Select an Inventory");
                return;
            }
            List<Product> products = productService.getProductsByInventory(currentInventory.getId());
            String currentIngredients = products.toString();
            try {
                resultTextArea.setValue("");
                String mealPlan = client.generateMealPlanBasedOnCurrentInventoryIngredients(currentIngredients);
                resultTextArea.setValue(mealPlan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        PDFGeneratorButton.addClickListener(event -> {
            String getResultTextToSave = resultTextArea.getValue();
            PDFGenerator.saveTextToPDF(getResultTextToSave, "StockMasterReport.pdf");
            System.out.println("Saving the Plan: " + getResultTextToSave);
            String succesfulAnnouncement = "You've downloaded the PDF file to your Download folder!";
            Notification.show(succesfulAnnouncement);
        });
        

        // Add buttons to the layout
        add(topic, button1, button2, button3, resultTextArea, PDFGeneratorButton);


        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }
}
