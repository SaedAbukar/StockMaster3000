package org.stockmaster3000.stockmaster3000.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository; // Mocked SupplierRepository dependency

    @InjectMocks
    private SupplierService supplierService; // SupplierService with injected mocks

    private Supplier supplier; // Test supplier instance

    @BeforeEach
    void setUp() {
        // Initialize a test supplier
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
    }

    @Test
    void testFindByName_Success() {
        // Mock repository to return a supplier when searched by a valid name
        when(supplierRepository.findByName("Test Supplier")).thenReturn(Optional.of(supplier));

        // Call the method to find the supplier by name
        Optional<Supplier> result = supplierService.findByName("Test Supplier");

        // Ensure the supplier was found and matches the expected values
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Supplier");

        // Verify that the repository method was called exactly once
        verify(supplierRepository, times(1)).findByName("Test Supplier");
    }

    @Test
    void testFindByName_NotFound() {
        // Mock repository to return an empty result when the supplier is not found
        when(supplierRepository.findByName("Nonexistent Supplier")).thenReturn(Optional.empty());

        // Call the method and verify that no supplier is found
        Optional<Supplier> result = supplierService.findByName("Nonexistent Supplier");

        // Ensure the result is empty
        assertThat(result).isEmpty();

        // Verify that the repository method was called exactly once
        verify(supplierRepository, times(1)).findByName("Nonexistent Supplier");
    }

    @Test
    void testSaveSupplier() {
        // Mock repository to return the saved supplier when calling save
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        // Call the method to save the supplier
        Supplier savedSupplier = supplierService.save(supplier);

        // Ensure the saved supplier has the expected values
        assertThat(savedSupplier).isNotNull();
        assertThat(savedSupplier.getId()).isEqualTo(1L);
        assertThat(savedSupplier.getName()).isEqualTo("Test Supplier");

        // Verify that the repository's save method was called once
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }
}
