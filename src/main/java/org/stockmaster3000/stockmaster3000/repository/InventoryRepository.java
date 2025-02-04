package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> getAllInventoriesByUserId(Long userId);
}
