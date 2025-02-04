package org.stockmaster3000.stockmaster3000.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.repository.ProductRepository;

import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Adds product to the inventory
    public void addProductToInventory(Product product, Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
            .orElseThrow(() -> new RuntimeException("Inventory not found"));

        // Setting the Inventory on the Product
        product.setInventory(inventory);

        // Save the product
        productRepository.save(product);
    }

    // Returns the list of products
    public List<Product> getAllProductsByInventoryId(Long inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if (inventory.isPresent()) {
            return productRepository.findByInventoryId(inventoryId); // Fetch products for the given inventory
        }
        throw new RuntimeException("Inventory not found");
    }

    // Deleting product by ID
    public void deleteProductByID(Long productId) {
        productRepository.deleteById(productId);
    }

    // Updating the quantity of the product
    @Transactional
    public void updateQuantity(Long productId, int newQuantity) {
        int updateRows = productRepository.updateProductQuantity(productId, newQuantity);
        if (updateRows == 0) {
            throw new RuntimeException("Product not found or not updated");
        }
    }

    // Returning a list of Products which are Expiring soon
    public List<Product> getExpiringSoonProducts() {
        return productRepository.getExpiringSoonProducts();
    }
}
