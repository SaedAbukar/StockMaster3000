package org.stockmaster3000.stockmaster3000.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final SupplierRepository supplierRepository;
  private final InventoryRepository inventoryRepository;
  private final CategoryRepository categoryRepository;

  // ProductService constructor
  public ProductService(ProductRepository productRepository,
                        SupplierRepository supplierRepository,
                        InventoryRepository inventoryRepository,
                        CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.supplierRepository = supplierRepository;
    this.inventoryRepository = inventoryRepository;
    this.categoryRepository = categoryRepository;
  }


  // Get all products in a specific inventory
  public List<Product> getProductsByInventory(Long inventoryId, String languageCode) {
    return productRepository.findByInventoryIdAndLanguageCode(inventoryId, languageCode);
  }

  // Fetching Product by Name
  public List<Product> getProductsByName(Long inventoryId, String productName,
                                         String languageCode) {
    if (productName != null && !productName.isEmpty()) {
      return productRepository.findByInventoryIdAndName(inventoryId, productName);
    } else {
      return productRepository.findByInventoryIdAndLanguageCode(inventoryId, languageCode);
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

    LocalDateTime firstCreatedAt = product.getCreatedAt();

    if (firstCreatedAt == null) {
      firstCreatedAt = LocalDateTime.now();
    }

    // Attach managed entities
    product.setInventory(inventory);
    product.setSupplier(supplier);
    product.setCategory(category);
    product.setCreatedAt(firstCreatedAt);
    product.setUpdatedAt(LocalDateTime.now());

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
    return productRepository.findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(inventoryId,
        7);
  }

  // Get products that are low in stock (e.g., less than 5 units)
  public List<Product> getLowStockItems(Long inventoryId) {
    return productRepository.findByInventoryIdAndQuantityLessThan(inventoryId, 5);
  }

  // Get products that are out of stock (e.g., less than 1 units)
  public List<Product> getOutOfStockItems(Long id) {
    return productRepository.findByInventoryIdAndQuantityLessThan(id, 1);
  }

  // Method to fetch product data categorized by inventory
  public Map<Category, Integer> getProductDataByInventory(Long inventoryId, String languageCode) {
    List<Product> products = productRepository.findByInventoryIdAndLanguageCode(inventoryId,
        languageCode);
    Map<Category, Integer> productData = new HashMap<>();

    // Loop through products and categorize them by category
    for (Product product : products) {
      Category category = product.getCategory();
      // Update the count of products in this category
      productData.put(category, productData.getOrDefault(category, 0) + 1);
    }

    return productData;
  }

}
