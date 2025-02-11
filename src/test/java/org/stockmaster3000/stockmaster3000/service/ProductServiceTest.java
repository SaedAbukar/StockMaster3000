package org.stockmaster3000.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.ProductRepository;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Inventory inventory;
    private Supplier supplier;
    private Category category;

    @BeforeEach
    void setUp() {
        // Initialize test data
        inventory = new Inventory();
        inventory.setId(1L);

        supplier = new Supplier();
        supplier.setId(1L);

        category = new Category();
        category.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setInventory(inventory);
        product.setSupplier(supplier);
        product.setCategory(category);
        product.setQuantity(10);
    }

    @Test
    void testGetProductsByInventory() {
        // Mock repository to return products associated with an inventory ID
        when(productRepository.findByInventoryId(1L)).thenReturn(List.of(product));

        // Call the method to fetch products by inventory
        List<Product> result = productService.getProductsByInventory(1L);

        // Verify the result
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findByInventoryId(1L);
    }

    @Test
    void testGetProductById() {
        // Mock repository to return a product when searched by ID
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Call the method to fetch product by ID
        Optional<Product> result = productService.getProductById(1L);

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testAddProduct_Success() {
        // Mock repositories to return existing inventory, supplier, and category
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call the method to add a product
        Product savedProduct = productService.addProduct(product);

        // Verify the result
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProduct_InventoryNotFound() {
        // Mock inventory repository to return empty when inventory not found
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product));
        assertThat(exception.getMessage()).isEqualTo("Inventory not found");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProduct_SupplierNotFound() {
        // Mock repositories to simulate missing supplier
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product));
        assertThat(exception.getMessage()).isEqualTo("Supplier not found");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProduct_CategoryNotFound() {
        // Mock repositories to simulate missing category
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify that an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product));
        assertThat(exception.getMessage()).isEqualTo("Category not found");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        // Mock repository to return the updated product
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call the method to update the product
        Product updatedProduct = productService.updateProduct(product);

        // Verify the result
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductsByName_WithProductName() {
        // Mock repository to return products when a name is provided
        when(productRepository.findByInventoryIdAndName(1L, "Test Product"))
                .thenReturn(List.of(product));

        // Call the method
        List<Product> result = productService.getProductsByName(1L, "Test Product");

        // Verify the result
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findByInventoryIdAndName(1L, "Test Product");
    }

    @Test
    void testGetProductsByName_WithoutProductName() {
        // Mock repository to return all products for an inventory when no name is provided
        when(productRepository.findByInventoryId(1L)).thenReturn(List.of(product));

        // Call the method
        List<Product> result = productService.getProductsByName(1L, "");

        // Verify the result
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryId(1L);
    }

    @Test
    void testGetProductsByName_WithNullProductName() {
        // Mock repository to return all products when productName is null
        when(productRepository.findByInventoryId(1L)).thenReturn(List.of(product));

        // Call the method
        List<Product> result = productService.getProductsByName(1L, null);

        // Verify the result
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryId(1L);
    }

    @Test
    void testDeleteProduct() {
        // Mock repository to perform deletion without errors
        doNothing().when(productRepository).deleteById(1L);

        // Call the method to delete the product
        productService.deleteProduct(1L);

        // Verify that the delete method was called once
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetExpiringSoonItems() {
        // Mock repository to return products that are expiring soon
        when(productRepository.findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(1L, 7)).thenReturn(List.of(product));

        // Call the method to get expiring soon products
        List<Product> result = productService.getExpiringSoonItems(1L);

        // Verify the result
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(1L, 7);
    }

    @Test
    void testGetLowStockItems() {
        // Mock repository to return products with low stock
        when(productRepository.findByInventoryIdAndQuantityLessThan(1L, 5)).thenReturn(List.of(product));

        // Call the method to get low stock products
        List<Product> result = productService.getLowStockItems(1L);

        // Verify the result
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndQuantityLessThan(1L, 5);
    }

    @Test
    void testGetOutOfStockItems() {
        // Mock repository to return products that are out of stock
        when(productRepository.findByInventoryIdAndQuantityLessThan(1L, 1)).thenReturn(List.of(product));

        // Call the method to get out of stock products
        List<Product> result = productService.getOutOfStockItems(1L);

        // Verify the result
        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndQuantityLessThan(1L, 1);
    }
}
