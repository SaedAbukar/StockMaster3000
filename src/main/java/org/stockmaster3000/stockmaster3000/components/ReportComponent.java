package org.stockmaster3000.stockmaster3000.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;


public class ReportComponent extends VerticalLayout {

    @Autowired
    private OpenAIClient client;

    public ReportComponent(OpenAIClient client) {
        this.client = client;

        // Giving the Report tab topic
        H3 topic = new H3("Generate Reports with AI!");

        // Initializing the text area for displaying generated content
        TextArea resultTextArea = new TextArea("AI Generated Report");
        resultTextArea.setWidthFull();
        resultTextArea.setHeightFull();
        resultTextArea.setReadOnly(true);

        // Initializing the buttons
        Button button1 = new Button("Get shopping list for the next 7 days + meal plan");
        Button button2 = new Button("Analyze your past 30 days ingredients healthiness!");
        Button button3 = new Button("Generate meal suggestions based on the current fridge ingredients!");
        Button PDFGeneratorButton = new Button("Download as PDF");

        // Click listeners for each button
        button1.addClickListener(event -> {
            Notification.show("Button 1 clicked!");
        });

        button2.addClickListener(event -> {
            Notification.show("Button 2 clicked!");
        });

        button3.addClickListener(event -> {
            String currentIngredients = "[{Chicken: 5, Meat: 8, Brocolli: 6}]"; 
            try {
                resultTextArea.setValue("");
                String mealPlan = client.generateMealPlanBasedOnCurrentInventoryIngredients(currentIngredients);
                resultTextArea.setValue(mealPlan);
                System.out.println("asdafqsa: " + mealPlan);
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
