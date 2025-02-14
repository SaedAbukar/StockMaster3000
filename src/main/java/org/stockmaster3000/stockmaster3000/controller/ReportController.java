package org.stockmaster3000.stockmaster3000.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stockmaster3000.stockmaster3000.service.ReportService;

// THIS WAS GENERATED FOR TESTING PURPOSES WITH POSTMAN OR ETC. DELETE WHEN READY.

@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/getNutritions")
    public ResponseEntity<String> getNutritions(@RequestParam String food) throws Exception {
        // Get the nutrition report (raw response)
        String nutritionReport = reportService.generateNutritions(food);

        // Log the raw response for debugging
        System.out.println("Nutrition Report: " + nutritionReport);

        // Return the nutritionReport as a plain string response
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)  // Explicitly set the content type
                .body(nutritionReport);  // Return the raw nutrition report string
    }


}
