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

    public String generateInventoryPlanningSuggestions1(String consumptionHistory, String currentMonth) throws Exception {
        String prompt = String.format(
            "Generate a shopping list (ingredients and amount to purchase) and meal plan based on your suggested ingredients for the next 7 days based on the following consumption history: %s. Also, suggest seasonal ingredients for the current month %s. Generate the response no longer than the size of 1 A4 paper.",
            consumptionHistory, currentMonth
        );
        return openAIClient.generateReport(prompt);
    }

    // TODO: Implement 2 other generations based on the instructions
    public String generateShoppingList(String consumptionHistory, String currentMonth) throws Exception {
        String prompt = String.format(
            "Generate a shopping list for the next 7 days based on the following consumption history: %s. Also, suggest seasonal ingredients for %s.",
            consumptionHistory, currentMonth
        );
        return openAIClient.generateReport(prompt);
    }

    public String generateMealPlan(String ingredients, String currentMonth) throws Exception {
        String prompt = String.format(
            "Create a 7-day meal plan using the following ingredients: %s. Include recipes for each meal and consider seasonal ingredients for %s.",
            ingredients, currentMonth
        );
        return openAIClient.generateReport(prompt);
    }
}