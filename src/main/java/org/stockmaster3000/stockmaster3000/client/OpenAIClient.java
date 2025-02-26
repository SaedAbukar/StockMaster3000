package org.stockmaster3000.stockmaster3000.client;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.security.PermitAll;

import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@PermitAll
public class OpenAIClient {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // The beneath methods will pass the needed information into this method for generating the AI response
    public String generateResponse(String prompt) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4-turbo");
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are an expert in inventory and meal planning. This prompt is for my inventory application report so I need you to generate only what I request."));
        messages.add(Map.of("role", "user", "content", prompt));
        
        payload.put("messages", messages);
        payload.put("max_tokens", 500);
        
        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        return extractContent(response.body());
    }

    // Parsing the JSON response
    private String extractContent(String apiResponse) throws JsonProcessingException {
        Map<String, Object> responseMap = objectMapper.readValue(apiResponse, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    }

    // 1. Button - Suggests what items to purchase on the next shopping according to the past 30 days activities and generates the meal plans also
    public String generateInventoryPlanningSuggestionsAndMealPlans(String consumptionHistory, String currentMonth) throws Exception {
        String prompt = String.format(
            "Analyse and generate a shopping list for the next 7 days based on the following consumption history: %s. Also, suggest seasonal ingredients for the current month %s and Generate the Meal Plan suggestion based on the ingredients you provided." + 
            "Your part is to only generate the Shopping list for 7 days, suggest the seasonal ingredients and meal plan, do not generate anything extra and no need to add # and ** marks since it will show raw on the report.",
            consumptionHistory, currentMonth
        );
        return generateResponse(prompt);
    }

    // 2. Button - Analyses the inventory Healthiness from the past 30 days
    public String generateInventoryHealthinessAnalysis(String consumptionHistory) throws Exception {
        String prompt = String.format(
            "Analyse the provided inventory food from the past 30 days and write a report based on its healthiness: %s. Your job is to return only the analys part and conclusion, do not generate anything extra and no need to add # and ** marks since it will show raw on the report.",
            consumptionHistory
        );
        return generateResponse(prompt);
    }

    // 3. Button - Generates the Meal suggestion based on the current inventory ingredients
    public String generateMealPlanBasedOnCurrentInventoryIngredients(String currentIngredients) throws Exception {
        String prompt = String.format(
            "Generate 1-8 meal suggestions based on the current ingredients in the fridge and take into consideration the quantities if the meal is possible to prepare: %s." +
            "Please return the respond in a String and without **",
            currentIngredients
        );
        return generateResponse(prompt);
    }

    public String getNutritions(String ingredient) throws Exception {
        String prompt = String.format(
            "Generate the nutritions for %s and specificly provide it in this form and only the requested fields without anything extra: { \\\"calories\\\": int, \\\"protein\\\": double, \\\"fat\\\": double, \\\"carbohydrates\\\": double}",
            ingredient
        );
        return generateResponse(prompt);
    }
}
