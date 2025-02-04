package org.stockmaster3000.stockmaster3000.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.service.ProductService;
import org.stockmaster3000.stockmaster3000.service.InventoryService;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    // Returns all of the products of the inventory
    @GetMapping("/{inventoryId}/products")
    public ResponseEntity<List<Product>> getAllProductsByInventory(@PathVariable Long inventoryId) {
        List<Product> products = productService.getAllProductsByInventoryId(inventoryId);
        return ResponseEntity.ok(products); // Return products for the given inventory
    }

    // Get inventory name
    @GetMapping("/{inventoryId}/name")
    public String getInventoryName(@PathVariable Long inventoryId) {
        return inventoryService.getInventoryName(inventoryId);
    }
}
