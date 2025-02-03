package org.stockmaster3000.stockmaster3000.service;

import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Get all products in a specific inventory
    public List<Product> getProductsByInventory(Long inventoryId) {
        return productRepository.findByInventoryId(inventoryId);
    }

    // Get a single product by ID
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // Add a new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete a product by ID
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // Get products that are expiring soon (e.g., less than 7 days left)
    public List<Product> getExpiringSoonItems(Long inventoryId) {
        return productRepository.findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(inventoryId, 7);
    }

    // Get products that are low in stock (e.g., less than 5 units)
    public List<Product> getLowStockItems(Long inventoryId) {
        return productRepository.findByInventoryIdAndQuantityLessThan(inventoryId, 5);
    }


    public List<Product> getOutOfStockItems(Long id) {
        return productRepository.findByInventoryIdAndQuantityLessThan(id, 1);
    }
}
