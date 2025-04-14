package org.stockmaster3000.stockmaster3000.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;

/**
 * Repository interface for accessing Inventory data.
 *
 * <p>Provides methods to perform CRUD operations on Inventory entities.
 * Uses Spring Data JPA for automatic implementation.</p>
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

  /**
   * Finds all inventories that belong to a specific user.
   *
   * @param user the user whose inventories should be fetched
   * @return list of inventories for the given user
   */
  List<Inventory> findByUser(User user);
}
