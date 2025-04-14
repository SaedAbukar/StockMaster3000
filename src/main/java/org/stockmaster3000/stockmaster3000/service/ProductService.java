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

/**
 * Service layer for managing products.
 *
 * <p>Handles creation, retrieval, updating, and deletion of product entities,
 * as well as inventory-related queries like stock levels and category summaries.</p>
 */
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final SupplierRepository supplierRepository;
  private final InventoryRepository inventoryRepository;
  private final CategoryRepository categoryRepository;

  /**
   * Creates a new {@code ProductService} with the required repositories.
   *
   * @param productRepository the product repository
   * @param supplierRepository the supplier repository
   * @param inventoryRepository the inventory repository
   * @param categoryRepository the category repository
   */
  public ProductService(ProductRepository productRepository,
                        SupplierRepository supplierRepository,
                        InventoryRepository inventoryRepository,
                        CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.supplierRepository = supplierRepository;
    this.inventoryRepository = inventoryRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Retrieves all products for a specific inventory and language.
   *
   * @param inventoryId the ID of the inventory
   * @param languageCode the language code for filtering products
   * @return list of matching products
   */
  public List<Product> getProductsByInventory(Long inventoryId, String languageCode) {
    return productRepository.findByInventoryIdAndLanguageCode(inventoryId, languageCode);
  }

  /**
   * Searches for products by name or returns all products for an inventory if name is empty.
   *
   * @param inventoryId the ID of the inventory
   * @param productName the name to search for
   * @param languageCode fallback language code if name is not provided
   * @return list of matching products
   */
  public List<Product> getProductsByName(
      Long inventoryId,
      String productName,
      String languageCode
  ) {
    if (productName != null && !productName.isEmpty()) {
      return productRepository.findByInventoryIdAndName(inventoryId, productName);
    } else {
      return productRepository.findByInventoryIdAndLanguageCode(inventoryId, languageCode);
    }
  }

  /**
   * Retrieves a single product by its ID.
   *
   * @param productId the product ID
   * @return an optional product if found
   */
  public Optional<Product> getProductById(Long productId) {
    return productRepository.findById(productId);
  }

  /**
   * Adds a new product to the database.
   *
   * @param product the product to add
   * @return the saved product
   */
  @Transactional
  public Product addProduct(Product product) {
    Inventory inventory = inventoryRepository.findById(product.getInventory().getId())
        .orElseThrow(() -> new RuntimeException("Inventory not found"));
    product.setInventory(inventory);

    Supplier supplier = supplierRepository.findById(product.getSupplier().getId())
        .orElseThrow(() -> new RuntimeException("Supplier not found"));
    product.setSupplier(supplier);

    Category category = categoryRepository.findById(product.getCategory().getId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    product.setCategory(category);

    LocalDateTime firstCreatedAt = product.getCreatedAt();
    if (firstCreatedAt == null) {
      firstCreatedAt = LocalDateTime.now();
    }

    product.setCreatedAt(firstCreatedAt);
    product.setUpdatedAt(LocalDateTime.now());

    return productRepository.save(product);
  }

  /**
   * Updates an existing product.
   *
   * @param product the product to update
   * @return the updated product
   */
  public Product updateProduct(Product product) {
    return productRepository.save(product);
  }

  /**
   * Deletes a product by its ID.
   *
   * @param productId the ID of the product to delete
   */
  public void deleteProduct(Long productId) {
    productRepository.deleteById(productId);
  }

  /**
   * Gets all products that are expiring soon (in less than 7 days).
   *
   * @param invId the ID of the inventory
   * @return list of soon-to-expire products
   */
  public List<Product> getExpiringSoonItems(Long invId) {
    return productRepository.findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(invId, 7);
  }

  /**
   * Gets products that are low in stock (less than 5 units).
   *
   * @param inventoryId the ID of the inventory
   * @return list of low-stock products
   */
  public List<Product> getLowStockItems(Long inventoryId) {
    return productRepository.findByInventoryIdAndQuantityLessThan(inventoryId, 5);
  }

  /**
   * Gets products that are out of stock.
   *
   * @param id the ID of the inventory
   * @return list of out-of-stock products
   */
  public List<Product> getOutOfStockItems(Long id) {
    return productRepository.findByInventoryIdAndQuantityLessThan(id, 1);
  }

  /**
   * Retrieves product counts grouped by category for a given inventory.
   *
   * @param inventoryId the ID of the inventory
   * @param langCode the language code for filtering
   * @return a map of categories to product count
   */
  public Map<Category, Integer> getProductDataByInventory(Long inventoryId, String langCode) {

    List<Product> products =
        productRepository.findByInventoryIdAndLanguageCode(inventoryId, langCode);

    Map<Category, Integer> productData = new HashMap<>();

    for (Product product : products) {
      Category category = product.getCategory();
      productData.put(category, productData.getOrDefault(category, 0) + 1);
    }

    return productData;
  }
}
