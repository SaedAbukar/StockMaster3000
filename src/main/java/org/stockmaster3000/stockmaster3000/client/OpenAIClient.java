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
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@PermitAll
public class OpenAIClient {

    private static final Dotenv dotenv = loadDotenv();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Dotenv loadDotenv() {
        // Try to load `.env` from the default location (works in Jenkins)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        if (dotenv.get("OPENAI_API_KEY") != null) {
            return dotenv;
        }

        // Try to load from `Stockmaster3000/Stockmaster3000` (for local development)
        dotenv = Dotenv.configure().directory("Stockmaster3000/Stockmaster3000").ignoreIfMissing().load();

        if (dotenv.get("OPENAI_API_KEY") != null) {
            return dotenv;
        }

        // Absolute path fallback (modify if needed)
        String absPath = "C:/Users/Käyttäjä/Desktop/StockMaster3000/StockMaster3000/.env";
        if (Files.exists(Paths.get(absPath))) {
            dotenv = Dotenv.configure().directory("C:/Users/Käyttäjä/Desktop/StockMaster3000/StockMaster3000").ignoreIfMissing().load();
            return dotenv;
        }

        throw new RuntimeException("❌ ERROR: OPENAI_API_KEY not found in any expected .env locations.");
    }

    // Every other method query calls this method at the end for generating the respond to API
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

    private String extractContent(String apiResponse) throws JsonProcessingException {
        Map<String, Object> responseMap = objectMapper.readValue(apiResponse, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    }

    // Instructions for generating Invnetory Planning Suggestions & Meal Plans
    public String generateInventoryPlanningSuggestionsAndMealPlans(String consumptionHistory, String currentMonth) throws Exception {
        String prompt = String.format(
            "Analyse and generate a shopping list for the next 7 days based on the following consumption history: %s. Also, suggest seasonal ingredients for the current month %s and Generate the Meal Plan suggestion based on the ingredients you provided." + 
            "Your part is to only generate the Shopping list for 7 days, suggest the seasonal ingredients and meal plan, do not generate anything extra and no need to add # and ** marks since it will show raw on the report.",
            consumptionHistory, currentMonth
        );
        return generateResponse(prompt);
    }

    // Instructions for Analysing Inventory Healthiness
    public String generateInventoryHealthinessAnalysis(String consumptionHistory) throws Exception {
        String prompt = String.format(
            "Analyse the provided inventory food from the past 30 days and write a report based on its healthiness: %s. Your job is to return only the analysis part and conclusion, do not generate anything extra and no need to add # and ** marks since it will show raw on the report.",
            consumptionHistory
        );
        return generateResponse(prompt);
    }

    // Instructions for generating Food suggestions based on the current inventory products
    public String generateMealPlanBasedOnCurrentInventoryIngredients(String currentIngredients) throws Exception {
        String prompt = String.format(
            "Generate 1-8 meal suggestions based on the current ingredients in the fridge and take into consideration the quantities if the meal is possible to prepare: %s." +
            "Please return the response in a String and without **",
            currentIngredients
        );
        return generateResponse(prompt);
    }

    // Generates the Nutritions upon product creation
    public String getNutritions(String ingredient) throws Exception {
        String prompt = String.format(
            "Generate the nutrition for %s per 100g (or per 100ml if it's a liquid/drink) and specifically provide it in this form and only the requested fields without anything extra: Calories: int, Protein: double, Fat: double, Carbohydrates: double",
            ingredient
        );
        return generateResponse(prompt);
    }
}
