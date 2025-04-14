package org.stockmaster3000.stockmaster3000.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Supplier;

/**
 * Repository interface for Supplier entity.
 *
 * <p>Provides CRUD operations and custom query methods for suppliers.</p>
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

  /**
   * Finds a supplier by its name.
   *
   * @param name the name of the supplier
   * @return an Optional containing the supplier if found, or empty otherwise
   */
  Optional<Supplier> findByName(String name);
}
