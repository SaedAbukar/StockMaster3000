package org.stockmaster3000.stockmaster3000.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class ReportComponent extends VerticalLayout {

    @Autowired
    private OpenAIClient client;

    public ReportComponent(OpenAIClient client) {
        this.client = client;

        // Giving the Report tab topic
        H3 topic = new H3("Generate Reports with AI!");

        // Initializing the buttons
        Button button1 = new Button("Get shopping list for the next 7 days + meal plan");
        Button button2 = new Button("Analyze your past 30 days ingredients healthiness!");
        Button button3 = new Button("Generate meal suggestions based on the current fridge ingredients!");

        // Click listeners for each button
        button1.addClickListener(event -> {
            Notification.show("Button 1 clicked!");
        });

        button2.addClickListener(event -> {
            Notification.show("Button 2 clicked!");
        });

        button3.addClickListener(event -> {
            String currentIngredients = "[\n" + //
                                "    {\n" + //
                                "        \"id\": 1,\n" + //
                                "        \"name\": \"Milk\",\n" + //
                                "        \"price\": 2.99,\n" + //
                                "        \"quantity\": 2,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 103,\n" + //
                                "            \"protein\": 8,\n" + //
                                "            \"fat\": 5,\n" + //
                                "            \"carbohydrates\": 12\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 7,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 101,\n" + //
                                "            \"name\": \"Local Dairy Co.\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 201,\n" + //
                                "            \"name\": \"Dairy\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-01T10:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-01T10:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 2,\n" + //
                                "        \"name\": \"Eggs\",\n" + //
                                "        \"price\": 3.49,\n" + //
                                "        \"quantity\": 12,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 70,\n" + //
                                "            \"protein\": 6,\n" + //
                                "            \"fat\": 5,\n" + //
                                "            \"carbohydrates\": 0\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 14,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 102,\n" + //
                                "            \"name\": \"Happy Hens Farm\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 202,\n" + //
                                "            \"name\": \"Protein\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-05T08:30:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-05T08:30:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 3,\n" + //
                                "        \"name\": \"Chicken Breast\",\n" + //
                                "        \"price\": 8.99,\n" + //
                                "        \"quantity\": 4,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 165,\n" + //
                                "            \"protein\": 31,\n" + //
                                "            \"fat\": 3.6,\n" + //
                                "            \"carbohydrates\": 0\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 5,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 103,\n" + //
                                "            \"name\": \"Fresh Poultry Inc.\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 202,\n" + //
                                "            \"name\": \"Protein\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-10T12:15:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-10T12:15:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 4,\n" + //
                                "        \"name\": \"Broccoli\",\n" + //
                                "        \"price\": 1.99,\n" + //
                                "        \"quantity\": 3,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 55,\n" + //
                                "            \"protein\": 4.3,\n" + //
                                "            \"fat\": 0.6,\n" + //
                                "            \"carbohydrates\": 11\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 10,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 104,\n" + //
                                "            \"name\": \"Green Valley Farms\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 203,\n" + //
                                "            \"name\": \"Vegetables\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-15T09:45:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-15T09:45:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 5,\n" + //
                                "        \"name\": \"Greek Yogurt\",\n" + //
                                "        \"price\": 4.49,\n" + //
                                "        \"quantity\": 6,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 100,\n" + //
                                "            \"protein\": 10,\n" + //
                                "            \"fat\": 0,\n" + //
                                "            \"carbohydrates\": 6\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 14,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 101,\n" + //
                                "            \"name\": \"Local Dairy Co.\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 201,\n" + //
                                "            \"name\": \"Dairy\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-20T11:20:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-20T11:20:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 6,\n" + //
                                "        \"name\": \"Cheddar Cheese\",\n" + //
                                "        \"price\": 5.99,\n" + //
                                "        \"quantity\": 1,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 113,\n" + //
                                "            \"protein\": 7,\n" + //
                                "            \"fat\": 9,\n" + //
                                "            \"carbohydrates\": 0.4\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 21,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 101,\n" + //
                                "            \"name\": \"Local Dairy Co.\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 201,\n" + //
                                "            \"name\": \"Dairy\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-22T14:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-22T14:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 7,\n" + //
                                "        \"name\": \"Butter\",\n" + //
                                "        \"price\": 3.49,\n" + //
                                "        \"quantity\": 1,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 102,\n" + //
                                "            \"protein\": 0.1,\n" + //
                                "            \"fat\": 11,\n" + //
                                "            \"carbohydrates\": 0.1\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 30,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 101,\n" + //
                                "            \"name\": \"Local Dairy Co.\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 201,\n" + //
                                "            \"name\": \"Dairy\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-25T16:30:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-25T16:30:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 8,\n" + //
                                "        \"name\": \"Salmon Fillet\",\n" + //
                                "        \"price\": 12.99,\n" + //
                                "        \"quantity\": 2,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 208,\n" + //
                                "            \"protein\": 20,\n" + //
                                "            \"fat\": 13,\n" + //
                                "            \"carbohydrates\": 0\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 3,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 105,\n" + //
                                "            \"name\": \"Ocean Fresh Seafood\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 202,\n" + //
                                "            \"name\": \"Protein\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-28T09:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-28T09:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 9,\n" + //
                                "        \"name\": \"Carrots\",\n" + //
                                "        \"price\": 1.49,\n" + //
                                "        \"quantity\": 5,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 41,\n" + //
                                "            \"protein\": 0.9,\n" + //
                                "            \"fat\": 0.2,\n" + //
                                "            \"carbohydrates\": 10\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 14,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 104,\n" + //
                                "            \"name\": \"Green Valley Farms\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 203,\n" + //
                                "            \"name\": \"Vegetables\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-09-30T12:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-09-30T12:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 10,\n" + //
                                "        \"name\": \"Apples\",\n" + //
                                "        \"price\": 2.99,\n" + //
                                "        \"quantity\": 6,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 95,\n" + //
                                "            \"protein\": 0.5,\n" + //
                                "            \"fat\": 0.3,\n" + //
                                "            \"carbohydrates\": 25\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 21,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 106,\n" + //
                                "            \"name\": \"Orchard Fresh\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 204,\n" + //
                                "            \"name\": \"Fruits\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-01T10:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-01T10:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 11,\n" + //
                                "        \"name\": \"Spinach\",\n" + //
                                "        \"price\": 2.49,\n" + //
                                "        \"quantity\": 2,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 23,\n" + //
                                "            \"protein\": 2.9,\n" + //
                                "            \"fat\": 0.4,\n" + //
                                "            \"carbohydrates\": 3.6\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 7,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 104,\n" + //
                                "            \"name\": \"Green Valley Farms\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 203,\n" + //
                                "            \"name\": \"Vegetables\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-03T14:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-03T14:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 12,\n" + //
                                "        \"name\": \"Orange Juice\",\n" + //
                                "        \"price\": 3.99,\n" + //
                                "        \"quantity\": 1,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 112,\n" + //
                                "            \"protein\": 1.7,\n" + //
                                "            \"fat\": 0.5,\n" + //
                                "            \"carbohydrates\": 26\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 10,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 107,\n" + //
                                "            \"name\": \"Sunny Grove\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 205,\n" + //
                                "            \"name\": \"Beverages\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-05T08:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-05T08:00:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 13,\n" + //
                                "        \"name\": \"Ground Beef\",\n" + //
                                "        \"price\": 7.99,\n" + //
                                "        \"quantity\": 2,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 250,\n" + //
                                "            \"protein\": 26,\n" + //
                                "            \"fat\": 15,\n" + //
                                "            \"carbohydrates\": 0\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 4,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 108,\n" + //
                                "            \"name\": \"Prime Cuts Butchery\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 202,\n" + //
                                "            \"name\": \"Protein\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-07T12:30:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-07T12:30:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 14,\n" + //
                                "        \"name\": \"Tomatoes\",\n" + //
                                "        \"price\": 1.99,\n" + //
                                "        \"quantity\": 4,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 18,\n" + //
                                "            \"protein\": 0.9,\n" + //
                                "            \"fat\": 0.2,\n" + //
                                "            \"carbohydrates\": 3.9\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 7,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 104,\n" + //
                                "            \"name\": \"Green Valley Farms\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 203,\n" + //
                                "            \"name\": \"Vegetables\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-09T09:45:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-09T09:45:00\"\n" + //
                                "    },\n" + //
                                "    {\n" + //
                                "        \"id\": 15,\n" + //
                                "        \"name\": \"Bacon\",\n" + //
                                "        \"price\": 6.49,\n" + //
                                "        \"quantity\": 1,\n" + //
                                "        \"nutritions\": {\n" + //
                                "            \"calories\": 42,\n" + //
                                "            \"protein\": 3,\n" + //
                                "            \"fat\": 3,\n" + //
                                "            \"carbohydrates\": 0.1\n" + //
                                "        },\n" + //
                                "        \"amountOfDaysUntilExpiration\": 7,\n" + //
                                "        \"supplier\": {\n" + //
                                "            \"id\": 108,\n" + //
                                "            \"name\": \"Prime Cuts Butchery\"\n" + //
                                "        },\n" + //
                                "        \"category\": {\n" + //
                                "            \"id\": 202,\n" + //
                                "            \"name\": \"Protein\"\n" + //
                                "        },\n" + //
                                "        \"inventory\": {\n" + //
                                "            \"id\": 301,\n" + //
                                "            \"name\": \"Home Fridge\"\n" + //
                                "        },\n" + //
                                "        \"createdAt\": \"2023-10-10T11:00:00\",\n" + //
                                "        \"updatedAt\": \"2023-10-10T11:00:00\"\n" + //
                                "    }\n" + //
                                "]"; 
            try {
                String mealPlan = client.generateMealPlanBasedOnCurrentInventoryIngredients(currentIngredients);
                Notification.show("Meal Plan: " + mealPlan);
                System.out.println("asdafqsa: " + mealPlan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        

        // Add buttons to the layout
        add(topic, button1, button2, button3);


        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }
}
