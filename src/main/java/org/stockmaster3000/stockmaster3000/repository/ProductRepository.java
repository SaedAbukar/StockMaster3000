package org.stockmaster3000.stockmaster3000.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Product;

/**
 * Repository interface for accessing {@link Product} data using Spring Data JPA.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Finds all products by inventory ID and language code.
   *
   * @param inventoryId the inventory ID
   * @param languageCode the language code
   * @return list of matching products
   */
  List<Product> findByInventoryIdAndLanguageCode(Long inventoryId, String languageCode);

  /**
   * Finds products by inventory ID and product name.
   *
   * @param inventoryId the inventory ID
   * @param name the name of the product
   * @return list of matching products
   */
  List<Product> findByInventoryIdAndName(Long inventoryId, String name);

  /**
   * Finds products that are expiring soon (less than specified days) in a given inventory.
   *
   * @param inventoryId the inventory ID
   * @param days threshold of days until expiration
   * @return list of expiring products
   */
  List<Product> findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(Long inventoryId, int days);

  /**
   * Finds products that are low in stock in a given inventory.
   *
   * @param inventoryId the inventory ID
   * @param quantity quantity threshold to define low stock
   * @return list of products with quantity below threshold
   */
  List<Product> findByInventoryIdAndQuantityLessThan(Long inventoryId, int quantity);

  /**
   * Finds all products belonging to a given inventory name.
   *
   * @param inventoryName the inventory name
   * @return collection of products
   */
  Collection<Object> findByInventoryName(String inventoryName);

  /**
   * Custom query to find products added after a certain date in a given inventory.
   *
   * @param inventoryId the inventory ID
   * @param sevenDaysAgo the LocalDateTime to compare with createdAt
   * @return list of newly added products
   */
  @Query(
      "SELECT p FROM Product p WHERE p.inventory.id = :inventoryId AND p.createdAt > :sevenDaysAgo"
  )
  List<Product> findByInventoryIdAndAddedDateAfter(
      @Param("inventoryId") Long inventoryId,
      @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
