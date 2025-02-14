package org.stockmaster3000.stockmaster3000.client;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class OpenAIClient {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Calling this function would return the nutritions
    public String generateFoodNutrition(String instructions) throws Exception {
        // Create the JSON payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4-turbo");

        // Prepare the list of messages (system and user roles)
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
            "role", "system",
            "content", "You are an expert nutritionist. Provide nutrition information in JSON format."
        ));
        messages.add(Map.of(
            "role", "user",
            "content", instructions
        ));

        // Add the messages to the payload
        payload.put("messages", messages);
        payload.put("max_tokens", 500);

        // Convert the payload to JSON String
        String requestBody = objectMapper.writeValueAsString(payload);

        // Send the request to OpenAI API
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the response to extract only the nutrition information
        return extractNutritionJson(response.body());
    }

    private String extractNutritionJson(String apiResponse) throws JsonProcessingException {
        // Parse the API response to extract the content
        Map<String, Object> responseMap = objectMapper.readValue(apiResponse, Map.class);
        
        // Cast choices to List<Map<String, Object>>
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        
        // Extract the content from the first choice
        String content = (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    
        // Return the content (which should be JSON)
        return content;
    }

    // TODO: Create the rest of the logic from ReportService 1 & 2
    
}