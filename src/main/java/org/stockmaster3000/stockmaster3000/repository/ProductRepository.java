package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
