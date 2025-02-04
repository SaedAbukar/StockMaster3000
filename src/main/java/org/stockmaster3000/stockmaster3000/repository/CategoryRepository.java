package org.stockmaster3000.stockmaster3000.repository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
