package org.stockmaster3000.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategoryByName_success() {
        // Mock repository to return a category when searched by name
        String categoryName = "Electronics";
        Category category = new Category();
        category.setName(categoryName);

        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.of(category));

        // Call method to find category by name
        Optional<Category> result = categoryService.findByName(categoryName);

        // Verify that the returned category exists and has the correct name
        assertTrue(result.isPresent(), "Category should be found");
        assertEquals(categoryName, result.get().getName(), "Category name should match");
        verify(categoryRepo, times(1)).findByName(categoryName); // Verify repository interaction
    }

    @Test
    void testGetCategoryByName_notFound() {
        // Mock repository to return empty when category does not exist
        String categoryName = "NonExistingCategory";
        when(categoryRepo.findByName(categoryName)).thenReturn(Optional.empty());

        // Call method to find category by name
        Optional<Category> result = categoryService.findByName(categoryName);

        // Verify that the category is not found
        assertTrue(result.isEmpty(), "Category should not be found");
        verify(categoryRepo, times(1)).findByName(categoryName); // Verify repository interaction
    }

    @Test
    void testSaveCategory_success() {
        // Mock repository to save and return the category
        Category category = new Category();
        category.setName("Books");

        when(categoryRepo.save(category)).thenReturn(category);

        // Call method to save category
        Category savedCategory = categoryService.save(category);

        // Verify that the returned category has the correct name
        assertEquals("Books", savedCategory.getName(), "Saved category name should match");
        verify(categoryRepo, times(1)).save(category); // Verify repository interaction
    }
}
