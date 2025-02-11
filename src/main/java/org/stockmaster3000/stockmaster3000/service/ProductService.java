package org.stockmaster3000.stockmaster3000.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.ProductRepository;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryRepository inventoryRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository, InventoryRepository inventoryRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryRepository = inventoryRepository;
        this.categoryRepository = categoryRepository;
    }

    // Get all products in a specific inventory
    public List<Product> getProductsByInventory(Long inventoryId) {
        return productRepository.findByInventoryId(inventoryId);
    }

    public List<Product> getProductsByName(Long inventoryId, String productName) {
        if (productName != null && !productName.isEmpty()) {
            return productRepository.findByInventoryIdAndName(inventoryId, productName);
        } else {
            return productRepository.findByInventoryId(inventoryId);
        }
    }

    // Get a single product by ID
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    // Add a new product
    @Transactional
    public Product addProduct(Product product) {
        Inventory inventory = inventoryRepository.findById(product.getInventory().getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Supplier supplier = supplierRepository.findById(product.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Attach managed entities
        product.setInventory(inventory);
        product.setSupplier(supplier);
        product.setCategory(category);

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
