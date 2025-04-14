package org.stockmaster3000.stockmaster3000.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;

/**
 * Service class for handling operations related to {@link Category}.
 *
 * <p>Provides methods to interact with the {@link CategoryRepository} for saving
 * and retrieving categories.</p>
 */
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * Creates a new instance of {@code CategoryService} with the given repository.
   *
   * @param categoryRepository the repository used for category persistence
   */
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * Finds a category by its name.
   *
   * @param name the name of the category
   * @return an {@code Optional} containing the found category, or empty if not found
   */
  public Optional<Category> findByName(String name) {
    return categoryRepository.findByName(name);
  }

  /**
   * Saves a category to the database.
   *
   * @param category the category to save
   * @return the saved category instance
   */
  public Category save(Category category) {
    return categoryRepository.save(category);
  }
}
