package org.stockmaster3000.stockmaster3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stockmaster3000.stockmaster3000.service.ReportService;

// THIS WAS GENERATED FOR TESTING PURPOSES WITH POSTMAN OR ETC.

@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/generate-shopping-list")
    public String generateShoppingList(
        @RequestParam String consumptionHistory,
        @RequestParam String currentMonth
    ) throws Exception {
        return reportService.generateShoppingList(consumptionHistory, currentMonth);
    }

    @GetMapping("/generate-meal-plan")
    public String generateMealPlan(
        @RequestParam String ingredients,
        @RequestParam String currentMonth
    ) throws Exception {
        return reportService.generateMealPlan(ingredients, currentMonth);
    }
}
