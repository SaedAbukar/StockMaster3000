package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Inventory;


import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // TODO: Needs fixing according to the UI logic

    // @Modifying
    // @Query("UPDATE Inventory i SET i.quantity = i.quantity + :quantity WHERE i.product.id = :productId")
    // int addStockByProductId(
    // @Param("productId") Long productId, 
    // @Param("quantity") int quantity
    // );

    // @Modifying
    // @Query("UPDATE Inventory i SET i.quantity = i.quantity - :quantity WHERE i.product.id = :productId")
    // int reduceStockByProductId(
    // @Param("productId") Long productId, 
    // @Param("quantity") int quantity
    // );
}
