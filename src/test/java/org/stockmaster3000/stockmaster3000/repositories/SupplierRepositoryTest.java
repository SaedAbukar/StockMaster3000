package org.stockmaster3000.stockmaster3000.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // This annotation is for testing JPA repositories
public class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    private Supplier supplier;

    @BeforeEach
    public void setUp() {
        supplier = new Supplier();
        supplier.setName("Fazer");
    }

    @Test
    public void testSaveSupplier() {
        Supplier savedSupplier = supplierRepository.save(supplier);
        assertNotNull(savedSupplier.getId(), "The supplier ID should not be null after saving");
        assertEquals("Fazer", savedSupplier.getName(), "The supplier name should be 'Fazer'");
    }

    // @Test
    // public void testFindSupplierByName() {
    //     supplierRepository.save(supplier);
    //     Supplier foundSupplier = supplierRepository.findByName("Fazer").orElse(null);
    //     assertNotNull(foundSupplier, "Supplier should be found by name");
    //     assertEquals("Supplier A", foundSupplier.getName(), "The supplier name should match 'Supplier A'");
    // }
}
