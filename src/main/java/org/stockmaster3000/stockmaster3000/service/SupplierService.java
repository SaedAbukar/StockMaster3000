package org.stockmaster3000.stockmaster3000.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

/**
 * Service for managing supplier-related operations.
 *
 * <p>Provides methods to save and retrieve suppliers by name.</p>
 */
@Service
public class SupplierService {

  private final SupplierRepository supplierRepository;

  /**
   * Creates a new {@code SupplierService} with the given repository.
   *
   * @param supplierRepository the repository used to access supplier data
   */
  public SupplierService(SupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  /**
   * Finds a supplier by its name.
   *
   * @param name the name of the supplier
   * @return an {@code Optional} containing the supplier if found
   */
  public Optional<Supplier> findByName(String name) {
    return supplierRepository.findByName(name);
  }

  /**
   * Saves a supplier to the database.
   *
   * @param supplier the supplier to save
   * @return the saved supplier
   */
  public Supplier save(Supplier supplier) {
    return supplierRepository.save(supplier);
  }
}
