package org.stockmaster3000.stockmaster3000.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByUser(User user);

}
