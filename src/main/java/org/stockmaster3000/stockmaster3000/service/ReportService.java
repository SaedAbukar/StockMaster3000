package org.stockmaster3000.stockmaster3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;

@Service
public class ReportService {

    private final OpenAIClient openAIClient;

    @Autowired
    public ReportService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    // 1. Generates food ingredients and amount of them suggestions for the next 7 days based on the previous 30 days data 
    public String generateInventoryPlanningSuggestions1(String consumptionHistory, String currentMonth) throws Exception {
        String prompt = String.format(
            "This prompt is for my inventory application report so I need you to generate only what I request. Analyse and generate a shopping list for the next 7 days based on the following consumption history: %s. Also, suggest seasonal ingredients for the current month %s. Generate the response no longer than the half size of A4 paper.",
            consumptionHistory, currentMonth
        );
        return openAIClient.generateFoodNutrition(prompt);
    }

    // Compining with the above generation - Generates meal plans for the next 7 days based on the suggested shopping list by OpenAi response
    public String generateMealPlansFor7Days(String shoppingListByGPT) throws Exception {
        String prompt = String.format(
            "This prompt is for my inventory application report so I need you to generate only what I request. Generate a meal suggestion plan for the next 7 days based on the following shopping list: %s. Generate the response no longer than half the size of A4 paper.",
            shoppingListByGPT
        );
        return openAIClient.generateFoodNutrition(prompt);
    }

    // 2. Analyse the past 30 days ingredients healthiness
    public String generateInventoryHealthinessForThePast30Days(String consumptionHistory) throws Exception {
        String prompt = String.format(
            "This prompt is for my inventory application report so I need you to generate only what I request. Analyse the provided inventory food from the past 30 days and write a report according to nutrition score: %s",
            consumptionHistory
        );
        return openAIClient.generateFoodNutrition(prompt);
    }

    // 3. Generating meal suggestions based on the leftovers or what is currently in the inventory
    public String generateMealPlan(String currentIngredients) throws Exception {
        String prompt = String.format(
            "This prompt is for my inventory application report so I need you to generate only what I request. Generate some meal suggestions based on these current food/leftovers in the fridge and also take into accound the quantity of them: %s.",
            currentIngredients
        );
        return openAIClient.generateFoodNutrition(prompt);
    }

    // Generates the nutritions for the placed parameter
    public String generateNutritions(String food) throws Exception {
        String prompt = String.format(
            "This prompt is for my inventory application report so I need you to generate only what I request and do not generate anything excess other than the JSON. Generate the nutritions for %s and specificly provide it in this form and only the requested fields: { \"calories\": int, \"protein\": double, \"fat\": double, \"carbohydrates\": double}",
            food
        );
        return openAIClient.generateFoodNutrition(prompt);
    }
}