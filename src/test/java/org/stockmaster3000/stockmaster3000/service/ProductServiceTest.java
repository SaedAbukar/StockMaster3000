package org.stockmaster3000.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        when(productRepository.findByInventoryId(1L)).thenReturn(List.of(product));

        List<Product> result = productService.getProductsByInventory(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findByInventoryId(1L);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testAddProduct_Success() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testAddProduct_InventoryNotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.addProduct(product));

        assertThat(exception.getMessage()).isEqualTo("Inventory not found");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product);

        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetExpiringSoonItems() {
        when(productRepository.findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(1L, 7)).thenReturn(List.of(product));

        List<Product> result = productService.getExpiringSoonItems(1L);

        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndAmountOfDaysUntilExpirationLessThan(1L, 7);
    }

    @Test
    void testGetLowStockItems() {
        when(productRepository.findByInventoryIdAndQuantityLessThan(1L, 5)).thenReturn(List.of(product));

        List<Product> result = productService.getLowStockItems(1L);

        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndQuantityLessThan(1L, 5);
    }

    @Test
    void testGetOutOfStockItems() {
        when(productRepository.findByInventoryIdAndQuantityLessThan(1L, 1)).thenReturn(List.of(product));

        List<Product> result = productService.getOutOfStockItems(1L);

        assertThat(result).hasSize(1);
        verify(productRepository, times(1)).findByInventoryIdAndQuantityLessThan(1L, 1);
    }
}
