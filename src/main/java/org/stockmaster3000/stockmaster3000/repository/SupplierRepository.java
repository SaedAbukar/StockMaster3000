package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByName(String name);
}


