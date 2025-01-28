package org.stockmaster3000.stockmaster3000;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.stockmaster3000.stockmaster3000.model.Product;
import org.stockmaster3000.stockmaster3000.repository.ProductRepository;
import org.stockmaster3000.stockmaster3000.repository.InventoryRepository;
import org.stockmaster3000.stockmaster3000.repository.CategoryRepository;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;
import org.stockmaster3000.stockmaster3000.repository.UserRepository;
import org.stockmaster3000.stockmaster3000.model.Category;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.model.Inventory;
import org.stockmaster3000.stockmaster3000.model.User;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    

    private Product product;
    private Category category;
    private Supplier supplier;
    private Inventory inventory;
    private User user;

    @BeforeEach
    public void setUp() {
        // Setting up required dependencies
        user = new User();
        user.setUsername("Viettranni");
        user.setPassword("Hello!");
        user = userRepository.save(user);

        supplier = new Supplier();
        supplier.setName("Fazer");
        supplier = supplierRepository.save(supplier);

        category = new Category();
        category.setName("Snacks");
        category = categoryRepository.save(category);

        inventory = new Inventory();
        inventory.setName("Fridge");
        inventory.setUser(user);
        inventory = inventoryRepository.save(inventory);

        // Create the product
        product = new Product();
        product.setName("Chocolate");
        product.setPrice(10.99);
        product.setQuantity(100);
        product.setAmountOfDaysUntilExpiration(45);
        product.setSupplier(supplier);
        product.setCategory(category);
        product.setInventory(inventory);
    }

    @Test
    public void testAddProduct() {
        // Save the product to the repository
        Product savedProduct = productRepository.save(product);

        // Verify that the product was saved with a generated ID
        assertNotNull(savedProduct.getId(), "The product ID should not be null after saving");
        
        // Verify that the product name is saved correctly
        assertEquals("Chocolate", savedProduct.getName(), "The product name should match the expected value");

        // Verify that the price and quantity are saved correctly
        assertEquals(10.99, savedProduct.getPrice(), "The product price should be 10.99");
        assertEquals(100, savedProduct.getQuantity(), "The product quantity should be 100");
        
        // Verify that the supplier is set correctly
        assertNotNull(savedProduct.getSupplier(), "The product should have a supplier set");
        assertEquals("Fazer", savedProduct.getSupplier().getName(), "The supplier name should be 'Fazer'");
    }
}
