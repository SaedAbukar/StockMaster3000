package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can add custom queries here if needed
    List<Product> findByInventoryId(Long inventoryId);

    List<Product> findByInventoryIdAndName(Long inventoryId, String name);

    List<Product> findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(Long inventoryId, int i);

    List<Product> findByInventoryIdAndQuantityLessThan(Long inventoryId, int i);

    Collection<Object> findByInventoryName(String inventoryName);

    // Custom query to fetch products added after the specified date for the given inventoryId
    @Query("SELECT p FROM Product p WHERE p.inventory.id = :inventoryId AND p.createdAt > :sevenDaysAgo")
    List<Product> findByInventoryIdAndAddedDateAfter(@Param("inventoryId") Long inventoryId, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);


}
