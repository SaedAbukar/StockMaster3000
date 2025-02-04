package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Product;

import jakarta.transaction.Transactional;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Finding Products by Inventory ID
    List<Product> findByInventoryId(Long inventoryId);

    // Returning the list of Products which are expiring soon
    @Query("SELECT p FROM Product p WHERE p.amountOfDaysUntilExpiration <= 3")
    List<Product> getExpiringSoonProducts();

    // Return a list of Products which is low on stock


    // Return a list of Products which is out of stock


    // Update the Product Quantity
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = :quantity WHERE p.id = :productId")
    int updateProductQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

}
