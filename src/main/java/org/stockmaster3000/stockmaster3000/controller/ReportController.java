package org.stockmaster3000.stockmaster3000.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stockmaster3000.stockmaster3000.client.OpenAIClient;

// THIS WAS GENERATED FOR TESTING PURPOSES WITH POSTMAN OR ETC. DELETE WHEN READY.

@RestController
public class ReportController {

    private final OpenAIClient client;

    @Autowired
    public ReportController(OpenAIClient client) {
        this.client = client;
    }

    @GetMapping("/getNutritions")
    public ResponseEntity<String> getNutritions(@RequestParam String food) throws Exception {
        // Get the nutrition report (raw response)
        String nutritionDetails = client.getNutritions(food);

        // Log the raw response for debugging
        System.out.println("Nutrition Report: " + nutritionDetails);

        // Return the nutritionReport as a plain string response
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)  // Explicitly set the content type
                .body(nutritionDetails);  // Return the raw nutrition report string
    }


    // @PostMapping("/generateMeals")
    // public ResponseEntity<String> generateMealPlanBasedOnInventory(@RequestBody List<Map<String, Object>> currentIngredients) throws Exception {
    //     String mealSuggestions = client.generateMealPlanBasedOnInventory(currentIngredients);

    //     // Log the raw response for debugging
    //     System.out.println("Suggested meals based on current ingredients: " + mealSuggestions);

    //     // Return the JSON response
    //     return ResponseEntity.ok()
    //             .contentType(MediaType.APPLICATION_JSON)  // Explicitly set the content type
    //             .body(mealSuggestions);
    // }

}
