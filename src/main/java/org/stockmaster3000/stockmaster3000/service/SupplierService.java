package org.stockmaster3000.stockmaster3000.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.stockmaster3000.stockmaster3000.model.Supplier;
import org.stockmaster3000.stockmaster3000.repository.SupplierRepository;

@Service
public class SupplierService {
  private final SupplierRepository supplierRepository;

  // Constructor
  public SupplierService(SupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  // Finding the Supplier by Name
  public Optional<Supplier> findByName(String name) {
    return supplierRepository.findByName(name);
  }

  // Saving the Supplier
  public Supplier save(Supplier supplier) {
    return supplierRepository.save(supplier);
  }
}
