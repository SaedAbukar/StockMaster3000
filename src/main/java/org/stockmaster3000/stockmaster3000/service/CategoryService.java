package org.stockmaster3000.stockmaster3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // Constructor for dependency injection
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}