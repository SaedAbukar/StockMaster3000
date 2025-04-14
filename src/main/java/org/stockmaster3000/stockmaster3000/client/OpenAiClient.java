package org.stockmaster3000.stockmaster3000.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.security.PermitAll;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Client for communicating with the OpenAI API to generate various inventory-related content.
 */
@Component
@PermitAll
public class OpenAiClient {

  private static final Dotenv dotenv = loadDotenv();
  private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Loads the .env file from various possible directories.
   *
   * @return loaded {@link Dotenv} instance containing environment variables
   */
  private static Dotenv loadDotenv() {
    // Try to load `.env` from the default location (works in Jenkins)
    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    if (dotenv.get("OPENAI_API_KEY") != null) {
      return dotenv;
    }

    // Try to load from `Stockmaster3000/Stockmaster3000` (for local development)
    dotenv = Dotenv.configure()
        .directory("Stockmaster3000/Stockmaster3000")
        .ignoreIfMissing()
        .load();

    if (dotenv.get("OPENAI_API_KEY") != null) {
      return dotenv;
    }

    // Absolute path fallback (modify if needed)
    String userHome = System.getProperty("user.home");
    Path dotenvPath = Paths.get(userHome, "Desktop", "StockMaster3000", "StockMaster3000", ".env");

    if (Files.exists(dotenvPath)) {
      Path parent = dotenvPath.getParent();
      if (parent != null) {
        dotenv = Dotenv.configure()
                .directory(parent.toString())
                .ignoreIfMissing()
                .load();
        return dotenv;
      }
    }

    throw new RuntimeException("ERROR: OPENAI_API_KEY not found in any expected .env locations.");
  }

  /**
   * Sends a prompt to OpenAI's Chat API and retrieves the generated response.
   *
   * @param prompt the prompt to send
   * @return the plain text response from the AI
   * @throws Exception if an error occurs during the HTTP request or JSON processing
   */
  public String generateResponse(String prompt) throws Exception {
    Map<String, Object> payload = new HashMap<>();
    payload.put("model", "gpt-4-turbo");

    List<Map<String, String>> messages = new ArrayList<>();
    messages.add(Map.of("role", "system", "content",
        "You are an expert in inventory and meal planning. "
            + "Please respond with plain text without any formatting like bold or headings."));

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

  /**
   * Extracts the text content from the OpenAI API response.
   *
   * @param apiResponse the raw JSON response
   * @return the extracted text content
   * @throws JsonProcessingException if parsing the response fails
   */
  private String extractContent(String apiResponse) throws JsonProcessingException {
    Map<String, Object> responseMap = objectMapper.readValue(apiResponse, Map.class);
    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
    return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
  }

  /**
   * Generates shopping list and meal plan suggestions based on inventory history and season.
   *
   * @param consumptionHistory a stringified list of products
   * @param currentMonth       the current month name
   * @param language           the target language for the response
   * @return generated shopping list and meal suggestions
   * @throws Exception if OpenAI request fails
   */
  public String generateInventoryPlanningSuggestionsAndMealPlans(
      String consumptionHistory,
      String currentMonth,
      String language
  ) throws Exception {
    String prompt = String.format(
        "Analyse and generate a shopping list for the next 7 days based on the following "
            + "consumption history: %s. Also, suggest seasonal ingredients for the current month %s"
            + " and Generate the Meal Plan suggestion based on the ingredients you provided."
            + "Your part is to only generate the Shopping list for 7 days, "
            + "suggest the seasonal ingredients and meal plan, "
            + "do not generate anything extra and no need to add # and ** marks "
            + "since it will show raw on the report. %s",
        consumptionHistory, currentMonth, language
    );
    return generateResponse(prompt);
  }

  /**
   * Generates a health analysis of the inventory based on consumption data.
   *
   * @param consumptionHistory the product log of the last 30 days
   * @param language           the target language
   * @return AI-generated health analysis
   * @throws Exception if OpenAI request fails
   */
  public String generateInventoryHealthinessAnalysis(String consumptionHistory, String language)
      throws Exception {
    String prompt = String.format(
        "Analyse the provided inventory food from the past 30 days "
        + "and write a report based on its healthiness: %s. "
        + "Your job is to return only the analysis part and conclusion, "
        + "do not generate anything extra "
        + "and no need to add # and ** marks since it will show raw on the report. %s",
        consumptionHistory, language
    );
    return generateResponse(prompt);
  }

  /**
   * Generates 1-8 meal suggestions based on current inventory ingredients.
   *
   * @param currentIngredients list of available items
   * @param language           the target language
   * @return generated meal suggestions
   * @throws Exception if OpenAI request fails
   */
  public String generateMealPlanBasedOnCurrentInventoryIngredients(String currentIngredients,
                                                                   String language)
      throws Exception {
    String prompt = String.format(
        "Generate 1-8 meal suggestions based on the current ingredients in the fridge and "
        + "take into consideration the quantities if the meal is possible to prepare: %s."
        + "Please return the response in a String and without ** %s",
        currentIngredients, language
    );
    return generateResponse(prompt);
  }

  /**
   * Fetches the nutritional data for a specific ingredient.
   *
   * @param ingredient the food or item to analyze
   * @param language   the language for response
   * @return nutritional values in a structured string format
   * @throws Exception if OpenAI request fails
   */
  public String getNutritions(String ingredient, String language) throws Exception {
    String prompt = String.format(
        "Generate the nutrition for %s per 100g (or per 100ml if it's a liquid/drink) "
        + "and specifically provide it in this form "
        + "and only the requested fields without anything extra: "
        + "Calories: int, Protein: double, Fat: double, Carbohydrates: double %s",
        ingredient, language
    );
    return generateResponse(prompt);
  }
}
