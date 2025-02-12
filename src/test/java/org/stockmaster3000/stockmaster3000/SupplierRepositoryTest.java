package org.stockmaster3000.stockmaster3000;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    private Supplier supplier;

    @BeforeEach
    public void setUp() {
        // Setting up a Supplier object before each test
        supplier = new Supplier();
        supplier.setName("Fazer");
    }

    @Test
    public void testSaveSupplier() {
        // Save the supplier to the repository
        Supplier savedSupplier = supplierRepository.save(supplier);
        
        // Verify that the supplier was saved with a generated ID
        assertNotNull(savedSupplier.getId(), "The supplier ID should not be null after saving");
        
        // Verify that the supplier name is saved correctly
        assertEquals("Fazer", savedSupplier.getName(), "The supplier name should be 'Fazer'");
    }

    @Test
    public void testFindById() {
        // Save the supplier to the repository
        Supplier savedSupplier = supplierRepository.save(supplier);
        
        // Retrieve the supplier by its ID
        Supplier foundSupplier = supplierRepository.findById(savedSupplier.getId()).orElse(null);
        
        // Verify that the supplier was found
        assertNotNull(foundSupplier, "Supplier should be found by ID");
        
        // Verify that the retrieved supplier's name matches the saved supplier's name
        assertEquals(savedSupplier.getName(), foundSupplier.getName(), "The names should match");
    }
}
