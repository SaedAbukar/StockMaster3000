package org.stockmaster3000.stockmaster3000.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Supplier;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
  Optional<Supplier> findByName(String name);
}


