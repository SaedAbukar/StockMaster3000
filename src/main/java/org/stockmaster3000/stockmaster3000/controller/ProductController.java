package org.stockmaster3000.stockmaster3000.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.stockmaster3000.stockmaster3000.service.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.stockmaster3000.stockmaster3000.model.Product;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Adding Product to Inventory
    @PostMapping("/{inventoryId}")
    public ResponseEntity<String> addProductToInventory(@RequestBody Product product, @PathVariable Long inventoryId) {
        productService.addProductToInventory(product, inventoryId);
        return ResponseEntity.ok("Product added successfully!");
    }

    // Fetching all of the products from that specific inventory
    @GetMapping("/{inventoryId}")
    public ResponseEntity<List<Product>> getAllProductsByInventory(@PathVariable Long inventoryId) {
        List<Product> products = productService.getAllProductsByInventoryId(inventoryId);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no products found
        }
        return ResponseEntity.ok(products);
    }


    // Deleting Product by ID
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductByID(@PathVariable Long productId) {
        productService.deleteProductByID(productId);
    }

    // Endpoint to update product quantity
    @PutMapping("/{id}/quantity/{newQuantity}")
    public ResponseEntity<String> updateQuantity(@PathVariable Long id, @PathVariable int newQuantity) {
        productService.updateQuantity(id, newQuantity); // Calls service method
        return ResponseEntity.ok("Quantity updated successfully!");
    }

    @GetMapping("/expiringSoonProducts")
    public ResponseEntity<List<Product>> getExpiringSoonProducts() {
        List<Product> expiringProducts = productService.getExpiringSoonProducts();
        return ResponseEntity.ok(expiringProducts);
    }
}
