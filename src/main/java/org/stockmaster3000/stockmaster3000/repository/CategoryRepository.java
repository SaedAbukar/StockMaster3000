package org.stockmaster3000.stockmaster3000.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.stockmaster3000.stockmaster3000.model.Category;

/**
 * Repository interface for managing {@link Category} entities.
 *
 * <p>Provides basic CRUD operations and custom queries for Category entities
 * using Spring Data JPA.</p>
 *
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Retrieves a category by its name.
   *
   * @param name the name of the category to retrieve
   * @return an {@code Optional} containing the matching category if found, otherwise empty
   */
  Optional<Category> findByName(String name);
}
