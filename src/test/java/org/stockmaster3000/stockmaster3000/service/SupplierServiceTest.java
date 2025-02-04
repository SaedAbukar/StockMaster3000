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
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
    }

    @Test
    void testFindByName_Success() {
        when(supplierRepository.findByName("Test Supplier")).thenReturn(Optional.of(supplier));

        Optional<Supplier> result = supplierService.findByName("Test Supplier");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Supplier");
        verify(supplierRepository, times(1)).findByName("Test Supplier");
    }

    @Test
    void testFindByName_NotFound() {
        when(supplierRepository.findByName("Nonexistent Supplier")).thenReturn(Optional.empty());

        Optional<Supplier> result = supplierService.findByName("Nonexistent Supplier");

        assertThat(result).isEmpty();
        verify(supplierRepository, times(1)).findByName("Nonexistent Supplier");
    }

    @Test
    void testSaveSupplier() {
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Supplier savedSupplier = supplierService.save(supplier);

        assertThat(savedSupplier).isNotNull();
        assertThat(savedSupplier.getId()).isEqualTo(1L);
        assertThat(savedSupplier.getName()).isEqualTo("Test Supplier");
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }
}
